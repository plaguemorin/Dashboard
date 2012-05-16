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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
		this.downloadUserStories(thisSprint);
	}


	@Override
	public List<UserStory> downloadUserStories(final Sprint sprint) {
		if (isEmpty(sprint.getSprintName())) {
			throw new IllegalArgumentException("Sprint name cannot be empty when using the JIRA Importer");
		}

		final List<RemoteIssue> issuesFromJqlSearch = this.jiraConnector.getIssuesFromJqlSearch("type =\"User story\" and fixVersion =\"" + sprint.getSprintName() + "\"", 1000);

		for (final RemoteIssue remoteIssue : issuesFromJqlSearch) {
			LOGGER.info("Got Issue: [" + remoteIssue.getKey() + "] " + remoteIssue.getSummary());
		}

		return null;
	}

	@Override
	public void findLatestSprint(final Sprint sprint) {
		if (isEmpty(sprint.getTeamName())) {
			throw new IllegalArgumentException("Sprint team name cannot be empty when using the JIRA Importer");
		}

		final Calendar today = new GregorianCalendar();
		today.setTime(new Date());

		final List<RemoteVersion> versions = this.jiraConnector.getVersions(sprint.getTeamName());
		for (final RemoteVersion version : versions) {
			LOGGER.info("Got Version: " + version.getId());
			if (version.getReleaseDate() != null && !version.isReleased() && !version.isArchived()) {
				if (version.getReleaseDate().after(today)) {
					LOGGER.info("Is after today ! " + version.getName());
					sprint.setSprintName(version.getName());

					return;
				}
			}
		}

		throw new IllegalStateException("No active sprint found !");
	}
}
