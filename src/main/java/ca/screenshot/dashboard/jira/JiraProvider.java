package ca.screenshot.dashboard.jira;

import ca.screenshot.dashboard.entity.*;
import ca.screenshot.dashboard.remote.jira.RemoteCustomFieldValue;
import ca.screenshot.dashboard.remote.jira.RemoteIssue;
import ca.screenshot.dashboard.service.provider.SprintProvider;
import ca.screenshot.dashboard.service.provider.UserStoryProvider;
import ca.screenshot.dashboard.service.repositories.ParticipantAPI;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Component
public class JiraProvider implements UserStoryProvider, SprintProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraProvider.class);

	@Autowired
	private ParticipantAPI participantAPI;

	@Autowired
	private SprintAPI sprintAPI;

	private JiraSoapConnector jiraSoapConnector;
	private JiraHttpConnector jiraHttpConnector;

	@Value("${jira.customField.storyPoints}")
	private String customFieldIdStoryPoints;

	@Value("${jira.customField.estimatedSeconds}")
	private String customFieldIdEstimatedSeconds;

	@Value("${jira.customField.remainingSeconds}")
	private String customFieldIdRemainingSeconds;

	@Value("${jira.systemId}")
	private String systemId;

	@Value("${jira.url}")
	private URL jiraUrl;

	@Value("${jira.userName}")
	private String jiraUsername;

	@Value("${jira.password}")
	private String jiraPassword;

	@Value("${jira.statusMap.done}")
	private String jiraStatusMapDone;

	@Value("${jira.statusMap.inProgress}")
	private String jiraStatusMapInProgress;

	@Value("${jira.statusMap.testing}")
	private String jiraStatusMapTesting;

	@PostConstruct
	public void init() throws MalformedURLException {
		this.jiraSoapConnector = new JiraSoapConnector();
		this.jiraSoapConnector.setJiraUrl(new URL(this.jiraUrl.getProtocol(), this.jiraUrl.getHost(), this.jiraUrl.getPort(), "/rpc/soap/jirasoapservice-v2?wsdl"));
		this.jiraSoapConnector.setUsername(this.jiraUsername);
		this.jiraSoapConnector.setPassword(this.jiraPassword);
		this.jiraSoapConnector.init();

		this.jiraHttpConnector = new JiraHttpConnector();
		this.jiraHttpConnector.setJiraUrl(new URL(this.jiraUrl.getProtocol(), this.jiraUrl.getHost(), this.jiraUrl.getPort(), "/sr/"));
		this.jiraHttpConnector.setUsername(this.jiraUsername);
		this.jiraHttpConnector.setPassword(this.jiraPassword);
		this.jiraHttpConnector.init();
	}

	@PreDestroy
	public void stop() {
		this.jiraSoapConnector.destroy();
		this.jiraHttpConnector.destroy();
	}

	@Override
	public void refreshUserStory(final UserStory userStory) {
		final RemoteReference remoteReference = userStory.getRemoteReferenceForSystemId(this.systemId);
		LOGGER.info("Getting tasks for user story " + remoteReference.getRemoteId());

		final Collection<String> currentTasks = new ArrayList<>();
		for (final UserStoryTask task : userStory.getTasks()) {
			final RemoteReference reference = task.getRemoteReferenceForSystemId(this.systemId);
			if (reference != null) {
				currentTasks.add(reference.getRemoteId());
			} else {
				LOGGER.debug("Skipping {} as it has no remote reference for system id {}", task.getTaskId(), this.systemId);
			}
		}

		final List<RemoteIssue> subIssues = this.jiraSoapConnector.getChildIssues(remoteReference.getRemoteId());
		for (final RemoteIssue subIssue : subIssues) {
			LOGGER.info("Got task: [{}] in status {}", new Object[]{subIssue.getKey(), subIssue.getSummary(), subIssue.getStatus()});
			if (currentTasks.contains(subIssue.getKey())) {
				LOGGER.debug("Task was not added since it was already in the list");
			} else {
				userStory.addTask(this.convertToTask(subIssue));
			}
		}

		final RemoteIssue issue = this.jiraSoapConnector.getIssue(remoteReference.getRemoteId());
		userStory.setTitle(issue.getSummary());
		userStory.setDescription(issue.getDescription());

		final Long storyPoints = this.extractStoryPoints(issue.getCustomFieldValues());
		if (storyPoints != null && storyPoints != 0L) {
			userStory.setStoryPoints(storyPoints.intValue());
		}

		remoteReference.clean();
	}

	@Override
	public void refreshTask(final UserStoryTask task) {
		final RemoteReference remoteReference = task.getRemoteReferenceForSystemId(this.systemId);
		LOGGER.info("Update info for task " + remoteReference.getRemoteId());

		populateUserStoryTaskFromIssue(task, this.jiraSoapConnector.getIssue(remoteReference.getRemoteId()));

		remoteReference.clean();
	}

	private UserStoryTask convertToTask(final RemoteIssue issue) {
		final UserStoryTask task = new UserStoryTask();
		LOGGER.debug("Converting issue {} to task", issue.getKey());

		task.addRemoteReference(this.systemId, issue.getKey());
		populateUserStoryTaskFromIssue(task, issue);

		return task;
	}

	private void populateUserStoryTaskFromIssue(final UserStoryTask task, final RemoteIssue issue) {
		task.setTitle(issue.getSummary());
		task.setSecondsEstimated(this.extractEstimatedSeconds(issue.getCustomFieldValues()));
		task.setSecondsRemaining(this.extractRemainingSeconds(issue.getCustomFieldValues()));

		if (issue.getAssignee() != null) {
			LOGGER.debug("Task is assigned to {}", issue.getAssignee());
			final Participant participantByUser = this.participantAPI.findParticipantByUser(issue.getAssignee());
			task.addWorkLog(participantByUser, 1L);
		}

		if (this.jiraStatusMapInProgress.equals(issue.getStatus()) && task.getStartDate() == null) {
			task.setStartDate(issue.getUpdated().getTime());
			LOGGER.debug("Issue was marked as in progress");
		}

		if (this.jiraStatusMapDone.equals(issue.getStatus()) && task.getDoneDate() == null) {
			task.setDoneDate(issue.getUpdated().getTime());
			LOGGER.debug("Issue was marked as done");
		}

		if (this.jiraStatusMapTesting.equals(issue.getStatus()) && task.getVerifyDate() == null) {
			task.setVerifyDate(issue.getUpdated().getTime());
			LOGGER.debug("Issue was marked as in testing");
		}
	}

	private Long extractRemainingSeconds(final RemoteCustomFieldValue[] customFieldValues) {
		return 0L;
	}

	private Long extractEstimatedSeconds(final RemoteCustomFieldValue[] customFieldValues) {
		for (final RemoteCustomFieldValue customFieldValue : customFieldValues) {
			if (this.customFieldIdEstimatedSeconds.equals(customFieldValue.getCustomfieldId())) {
				return Long.valueOf(customFieldValue.getValues()[0]);
			}
		}

		return 0L;
	}

	private Long extractStoryPoints(final RemoteCustomFieldValue[] customFieldValues) {
		for (final RemoteCustomFieldValue customFieldValue : customFieldValues) {
			if (this.customFieldIdStoryPoints.equals(customFieldValue.getCustomfieldId())) {
				return Long.valueOf(customFieldValue.getValues()[0]);
			}
		}

		return 0L;
	}

	@Override
	public void refreshSprint(final Sprint sprint) {
		final RemoteReference jiraRelease = sprint.getRemoteReferenceForSystemId(this.systemId);
		final List<RemoteIssue> issues = this.jiraSoapConnector.getIssuesFromJqlSearch("fixVersion = \"" + jiraRelease.getRemoteId() + "\"", 9000);

		for (final RemoteIssue issue : issues) {
			boolean found = false;
			for (final UserStory userStory : sprint.getUserStories()) {
				final RemoteReference reference = userStory.getRemoteReferenceForSystemId(this.systemId);
				if (reference != null && reference.getRemoteId().equals(issue.getKey())) {
					found = true;
					break;
				}
			}

		}
	}

	@Override
	public void setSystemId(final String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String getSystemId() {
		return this.systemId;
	}


}
