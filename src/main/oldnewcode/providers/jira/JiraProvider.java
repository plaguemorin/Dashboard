package ca.screenshot.dashboard.service.providers.jira;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.external.jira.RemoteCustomFieldValue;
import ca.screenshot.dashboard.external.jira.RemoteIssue;
import ca.screenshot.dashboard.external.jira.RemoteUser;
import ca.screenshot.dashboard.external.jira.RemoteVersion;
import ca.screenshot.dashboard.service.providers.ParticipantAugmenter;
import ca.screenshot.dashboard.service.providers.SprintProvider;
import ca.screenshot.dashboard.service.providers.UserStoryProvider;
import ca.screenshot.dashboard.service.repositories.ParticipantAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Service
public class JiraProvider implements UserStoryProvider, ParticipantAugmenter, SprintProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraProvider.class);

	@Autowired
	private JiraConnector jiraConnector;

	@Autowired
	private ParticipantAPI participantAPI;

	@Value("${jiraImporter.customFields.storyPoints}")
	private String customFieldIdStoryPoints;

	@Value("${jiraImporter.customFields.estimatedSeconds}")
	private String customFieldIdEstimatedSeconds;

	@Override
	public void augmentUserStory(UserStory userStory) {
		LOGGER.info("Getting tasks for user story " + userStory.getRemoteIdentifier("JIRA"));

		final List<RemoteIssue> subIssues = this.jiraConnector.getIssuesFromJqlSearch("parent =\"" + userStory.getRemoteIdentifier("JIRA") + "\"", 1000);
		for (final RemoteIssue subIssue : subIssues) {
			LOGGER.info("Got task: [" + subIssue.getKey() + "] " + subIssue.getSummary());
			userStory.addTask(this.convertToTask(subIssue));

			if (subIssue.getAssignee() != null) {
				userStory.addOrUpdateParticipant(this.participantAPI.findParticipantByUser(subIssue.getAssignee()));
			}
		}
	}

	private UserStoryTask convertToTask(RemoteIssue subIssue) {
		final UserStoryTask task = new UserStoryTask();

		task.setTitle(subIssue.getSummary());
		task.setRemoteIdentifier("JIRA", subIssue.getKey());
		task.setSecondsEstimated(this.extractEstimatedSeconds(subIssue.getCustomFieldValues()) * 60);

		return task;
	}

	private Long extractEstimatedSeconds(RemoteCustomFieldValue[] customFieldValues) {
		for (final RemoteCustomFieldValue customFieldValue : customFieldValues) {
			if (customFieldIdEstimatedSeconds.equals(customFieldValue.getCustomfieldId())) {
				return Long.valueOf(customFieldValue.getValues()[0]);
			}
		}

		return 0L;
	}

	private Long extractStoryPoints(RemoteCustomFieldValue[] customFieldValues) {
		for (final RemoteCustomFieldValue customFieldValue : customFieldValues) {
			if (customFieldIdStoryPoints.equals(customFieldValue.getCustomfieldId())) {
				return Long.valueOf(customFieldValue.getValues()[0]);
			}
		}

		return 0L;
	}

	private UserStory convertToUserStory(final RemoteIssue remoteIssue, Sprint sprint) {
		final UserStory us = new UserStory();

		if (remoteIssue.getAssignee() != null) {
			final Participant participant = this.participantAPI.findParticipantByUser(remoteIssue.getAssignee());
			sprint.addOrUpdateParticipant(participant);
			us.addOrUpdateParticipant(participant);
		}

		us.setRemoteIdentifier("JIRA", remoteIssue.getKey());
		us.setTitle(remoteIssue.getSummary().trim());
		us.setDescription(remoteIssue.getDescription());
		us.setStoryPoints(this.extractStoryPoints(remoteIssue.getCustomFieldValues()));

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

		final List<RemoteVersion> versions = this.jiraConnector.getVersions(teamName);
		final List<Sprint> consideredSprints = new ArrayList<>();

		for (final RemoteVersion version : versions) {
			if (version.getReleaseDate() != null && !version.isReleased() && !version.isArchived()) {
				final Sprint sprint = new Sprint();

				sprint.setSprintName(version.getName());
				sprint.setTeamName(teamName);
				sprint.setEndDate(version.getReleaseDate());
				sprint.setRemoteIdentifier("JIRA", version.getName());

				consideredSprints.add(sprint);
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

	@Override
	public void augmentSprint(Sprint theSprint) {
		if (isEmpty(theSprint.getSprintName()) || isEmpty(theSprint.getTeamName())) {
			throw new IllegalArgumentException("Sprint name and Team name cannot be empty when using the JIRA Importer");
		}

		final List<RemoteVersion> versions = this.jiraConnector.getVersions(theSprint.getTeamName());
		for (final RemoteVersion version : versions) {
			if (theSprint.getSprintName().equals(version.getName())) {
				theSprint.setEndDate(version.getReleaseDate());
				theSprint.setRemoteIdentifier("JIRA", version.getId());

				final List<RemoteIssue> issuesFromJqlSearch = this.jiraConnector.getIssuesFromJqlSearch("type =\"User story\" and fixVersion =\"" + theSprint.getSprintName() + "\"", 1000);
				for (final RemoteIssue remoteIssue : issuesFromJqlSearch) {
					LOGGER.info("Got Issue: [" + remoteIssue.getKey() + "] " + remoteIssue.getSummary());

					theSprint.addUserStory(this.convertToUserStory(remoteIssue, theSprint));
				}

				return;
			}
		}
	}

	@Override
	public void augmentParticipant(Participant user) {
		final RemoteUser remoteUser = this.jiraConnector.getUser(user.getUser());

		if (remoteUser != null) {
			user.setRemoteIdentifier("JIRA", remoteUser.getName());
			user.setUser(remoteUser.getName());
			user.setEmail(remoteUser.getEmail());
			user.setDisplayName(remoteUser.getFullname());
		}
	}

}
