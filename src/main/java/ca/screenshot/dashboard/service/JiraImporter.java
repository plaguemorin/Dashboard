package ca.screenshot.dashboard.service;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.external.jira.RemoteIssue;
import ca.screenshot.dashboard.external.jira.RemoteVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.Collections.sort;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Service
public class JiraImporter implements UserStoryImporter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraImporter.class);

	@Autowired
	private JiraConnector jiraConnector;

	@PostConstruct
	public void init() {
		final Sprint thisSprint = new Sprint();
		thisSprint.setTeamName("CANADIENS");

		this.findLatestSprint(thisSprint);
		this.updateUserStories(thisSprint);
	}


	@Override
	public void updateUserStories(final Sprint sprint) {
		if (isEmpty(sprint.getSprintName())) {
			throw new IllegalArgumentException("Sprint name cannot be empty when using the JIRA Importer");
		}

		final List<RemoteIssue> issuesFromJqlSearch = this.jiraConnector.getIssuesFromJqlSearch("type =\"User story\" and fixVersion =\"" + sprint.getSprintName() + "\"", 1000);

		for (final RemoteIssue remoteIssue : issuesFromJqlSearch) {
			LOGGER.info("Got Issue: [" + remoteIssue.getKey() + "] " + remoteIssue.getSummary());

			sprint.updateOrCreate(this.convertToUserStory(remoteIssue));
		}

	}

	private UserStory convertToUserStory(final RemoteIssue remoteIssue) {

		return null;
	}

	@Override
	public void findLatestSprint(final Sprint sprint) {
		final List<RemoteVersion> consideredSprints = findPossibleSprints(sprint);

		if (!consideredSprints.isEmpty()) {
			final RemoteVersion version = consideredSprints.get(0);
			sprint.setSprintName(version.getName());
			return;
		}

		throw new IllegalStateException("No active sprint found !");
	}

	/**
	 * Find versions that can be valid sprints
	 *
	 * @param sprint the current sprint
	 * @return ordered list of possible sprints active sprints from oldest to newest
	 */
	private List<RemoteVersion> findPossibleSprints(Sprint sprint) {
		if (isEmpty(sprint.getTeamName())) {
			throw new IllegalArgumentException("Sprint team name cannot be empty when using the JIRA Importer");
		}

		final Calendar today = new GregorianCalendar();
		today.setTime(new Date());

		final List<RemoteVersion> versions = this.jiraConnector.getVersions(sprint.getTeamName());
		final List<RemoteVersion> consideredSprints = new ArrayList<>();

		for (final RemoteVersion version : versions) {
			if (version.getReleaseDate() != null && !version.isReleased() && !version.isArchived()) {
				if (version.getReleaseDate().after(today)) {
					LOGGER.debug("Found possible sprint: \"" + version.getName() + "\"");
					consideredSprints.add(version);
				} else {
					LOGGER.debug("Discarded since release date is in the past: "
							+ version.getName()
							+ " (release date was: " + version.getReleaseDate().toString() + ")");
				}
			}
		}

		sort(consideredSprints, new Comparator<RemoteVersion>() {
			@Override
			public int compare(final RemoteVersion o1, final RemoteVersion o2) {
				final Calendar xcal = o1.getReleaseDate();
				final Calendar ycal = o2.getReleaseDate();

				if (xcal.before(ycal)) return -1;
				if (xcal.after(ycal)) return 1;
				return 0;
			}
		});

		return consideredSprints;
	}
}
