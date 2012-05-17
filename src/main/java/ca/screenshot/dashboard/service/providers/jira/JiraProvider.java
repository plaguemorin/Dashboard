package ca.screenshot.dashboard.service.providers.jira;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.external.jira.RemoteIssue;
import ca.screenshot.dashboard.external.jira.RemoteUser;
import ca.screenshot.dashboard.external.jira.RemoteVersion;
import ca.screenshot.dashboard.service.providers.ParticipantProvider;
import ca.screenshot.dashboard.service.providers.SprintProvider;
import ca.screenshot.dashboard.service.providers.UserStoryProvider;
import ca.screenshot.dashboard.service.repositories.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Collections.sort;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Service
public class JiraProvider implements UserStoryProvider, ParticipantProvider, SprintProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraProvider.class);

	@Autowired
	private JiraConnector jiraConnector;

	@Autowired
	private ParticipantRepository participantRepository;

	@Override
	public List<UserStory> getUserStoriesForSprint(final Sprint sprint) {
		if (isEmpty(sprint.getSprintName())) {
			throw new IllegalArgumentException("Sprint name cannot be empty when using the JIRA Importer");
		}

		final List<UserStory> theReturnList = new ArrayList<>();
		final List<RemoteIssue> issuesFromJqlSearch = this.jiraConnector.getIssuesFromJqlSearch("type =\"User story\" and fixVersion =\"" + sprint.getSprintName() + "\"", 1000);

		for (final RemoteIssue remoteIssue : issuesFromJqlSearch) {
			LOGGER.info("Got Issue: [" + remoteIssue.getKey() + "] " + remoteIssue.getSummary());

			theReturnList.add(this.convertToUserStory(remoteIssue, sprint));
		}

		// Populate Sub-tasks
		for (final UserStory userStory : theReturnList) {
			LOGGER.info("Getting tasks for user story " + userStory.getRemoteIdentifier());

			final List<RemoteIssue> subIssues = this.jiraConnector.getIssuesFromJqlSearch("parent =\"" + userStory.getRemoteIdentifier() + "\"", 1000);
			for (final RemoteIssue subIssue : subIssues) {
				LOGGER.info("Got task: [" + subIssue.getKey() + "] " + subIssue.getSummary());
				final Participant participant = this.participantRepository.findParticipantByUser(subIssue.getAssignee());

				userStory.addTask(this.convertToTask(subIssue));
				userStory.addOrUpdateParticipant(participant);
				sprint.addOrUpdateParticipant(participant);
			}
		}

		return theReturnList;
	}

	private UserStoryTask convertToTask(RemoteIssue subIssue) {
		final UserStoryTask task = new UserStoryTask();

		task.setGuid(subIssue.getId());
		task.setTitle(subIssue.getSummary());
		task.setRemoteIdentifier(subIssue.getKey());

		return task;
	}

	private UserStory convertToUserStory(final RemoteIssue remoteIssue, Sprint sprint) {
		final UserStory us = new UserStory();
		final Participant participant = this.participantRepository.findParticipantByUser(remoteIssue.getAssignee());

		sprint.addOrUpdateParticipant(participant);

		us.setRemoteIdentifier(remoteIssue.getKey());
		us.setTitle(remoteIssue.getSummary().trim());
		us.setDescription(remoteIssue.getDescription());
		us.addOrUpdateParticipant(participant);
		us.setGuid(remoteIssue.getId());

		return us;
	}

	@Override
	public Sprint findLatestSprint(final String teamName) {
		final List<Sprint> consideredSprints = findPossibleSprints(teamName);

		if (!consideredSprints.isEmpty()) {
			return consideredSprints.get(0);
		}

		throw new IllegalStateException("No active sprint found !");
	}

	/**
	 * Find versions that can be valid sprints
	 *
	 * @param teamName the current teamName
	 * @return ordered list of possible sprints active sprints from oldest to newest
	 */
	@Override
	public List<Sprint> findPossibleSprints(String teamName) {
		if (isEmpty(teamName)) {
			throw new IllegalArgumentException("Sprint team name cannot be empty when using the JIRA Importer");
		}

		final Calendar today = new GregorianCalendar();
		today.setTime(new Date());

		final List<RemoteVersion> versions = this.jiraConnector.getVersions(teamName);
		final List<Sprint> consideredSprints = new ArrayList<>();

		for (final RemoteVersion version : versions) {
			if (version.getReleaseDate() != null && !version.isReleased() && !version.isArchived()) {
				if (version.getReleaseDate().after(today)) {
					LOGGER.info("Found possible teamName: \"" + version.getName() + "\"");
					consideredSprints.add(this.convertToSprint(version, teamName));
				} else {
					LOGGER.info("Discarded since release date is in the past: "
							+ version.getName()
							+ " (release date was: " + version.getReleaseDate().toString() + ")");
				}
			}
		}

		sort(consideredSprints, new Comparator<Sprint>() {
			@Override
			public int compare(final Sprint o1, final Sprint o2) {
				final Calendar xcal = o1.getEndDate();
				final Calendar ycal = o2.getEndDate();

				if (xcal.before(ycal)) return -1;
				if (xcal.after(ycal)) return 1;
				return 0;
			}
		});

		return consideredSprints;
	}

	private Sprint convertToSprint(RemoteVersion version, String teamName) {
		final Sprint sprint = new Sprint();
		sprint.setSprintName(version.getName());
		sprint.setTeamName(teamName);
		sprint.setEndDate(version.getReleaseDate());
		sprint.setGuid(version.getId());

		return sprint;
	}

	@Override
	public Participant findParticipantByUser(String user) {
		final RemoteUser remoteUser = this.jiraConnector.getUser(user);

		if (remoteUser != null) {
			return convertToParticipant(remoteUser);
		}

		return null;
	}

	private Participant convertToParticipant(RemoteUser remoteUser) {
		final Participant participant = new Participant();
		participant.setUser(remoteUser.getName());
		participant.setEmail(remoteUser.getEmail());
		participant.setDisplayName(remoteUser.getFullname());
		participant.setGuid(remoteUser.getName() + "|JIRA");
		return participant;
	}
}
