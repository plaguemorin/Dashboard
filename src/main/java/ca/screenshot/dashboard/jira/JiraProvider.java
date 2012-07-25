package ca.screenshot.dashboard.jira;

import ca.screenshot.dashboard.entity.RemoteReference;
import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.remote.jira.RemoteCustomFieldValue;
import ca.screenshot.dashboard.remote.jira.RemoteIssue;
import ca.screenshot.dashboard.remote.jira.RemoteStatus;
import ca.screenshot.dashboard.service.provider.SprintProvider;
import ca.screenshot.dashboard.service.provider.UserStoryProvider;
import org.apache.axis.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Component
public class JiraProvider implements UserStoryProvider, SprintProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraProvider.class);

	private JiraConnector jiraConnector;

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

	private Map<String, RemoteStatus> statusMap = new HashMap<>();

	@PostConstruct
	public void init() throws AxisFault {
		this.jiraConnector = new JiraConnector();

		this.jiraConnector.setJiraUrl(this.jiraUrl);
		this.jiraConnector.setUsername(this.jiraUsername);
		this.jiraConnector.setPassword(jiraPassword);

		this.jiraConnector.init();

		for (final RemoteStatus status : this.jiraConnector.getRemoteStatus()) {
			this.statusMap.put(status.getId(), status);
		}
	}

	@PreDestroy
	public void stop() throws RemoteException {
		this.jiraConnector.destory();
	}

	@Override
	public void refreshUserStory(final UserStory userStory) {
		final RemoteReference remoteReference = userStory.getRemoteReferenceForSystemId(this.getSystemId());
		LOGGER.info("Getting tasks for user story " + remoteReference.getRemoteId());

		final List<String> currentTasks = new ArrayList<>();
		for (final UserStoryTask task : userStory.getTasks()) {
			final RemoteReference reference = task.getRemoteReferenceForSystemId(this.getSystemId());
			if (reference != null) {
				currentTasks.add(reference.getRemoteId());
			}
		}

		final List<RemoteIssue> subIssues = this.jiraConnector.getChildIssues(remoteReference.getRemoteId());
		for (final RemoteIssue subIssue : subIssues) {
			LOGGER.info("Got task: [" + subIssue.getKey() + "] " + subIssue.getSummary());
			if (!currentTasks.contains(subIssue.getKey())) {
				userStory.addTask(this.convertToTask(subIssue));
			}

			if (subIssue.getAssignee() != null) {
				//userStory.addOrUpdateParticipant(this.participantAPI.findParticipantByUser(issue.getAssignee()));
			}
		}

		final RemoteIssue issue = this.jiraConnector.getIssue(remoteReference.getRemoteId());
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
		final RemoteReference remoteReference = task.getRemoteReferenceForSystemId(this.getSystemId());
		LOGGER.info("Update info for task " + remoteReference.getRemoteId());

		populateUserStoryTaskFromIssue(task, this.jiraConnector.getIssue(remoteReference.getRemoteId()));

		remoteReference.clean();
	}

	private UserStoryTask convertToTask(final RemoteIssue issue) {
		final UserStoryTask task = new UserStoryTask();

		task.addRemoteReference(this.getSystemId(), issue.getKey());
		populateUserStoryTaskFromIssue(task, issue);

		return task;
	}

	private void populateUserStoryTaskFromIssue(UserStoryTask task, RemoteIssue issue) {
		task.setTitle(issue.getSummary());
		task.setSecondsEstimated(this.extractEstimatedSeconds(issue.getCustomFieldValues()));
		task.setSecondsRemaining(this.extractRemainingSeconds(issue.getCustomFieldValues()));

		if (this.jiraStatusMapInProgress.equals(issue.getStatus()) && task.isStarted()) {
			task.setStartDate(issue.getUpdated().getTime());
		}

		if (this.jiraStatusMapDone.equals(issue.getStatus()) && !task.isDone()) {
			task.setDoneDate(issue.getUpdated().getTime());
		}
	}

	private Long extractRemainingSeconds(RemoteCustomFieldValue[] customFieldValues) {
		return 0L;
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

	@Override
	public void refreshSprint(Sprint sprint) {
		final RemoteReference jiraRelease = sprint.getRemoteReferenceForSystemId(this.getSystemId());
		final List<RemoteIssue> issues = this.jiraConnector.getIssuesFromJqlSearch("fixVersion = \"" + jiraRelease.getRemoteId() + "\"", 9000);

		for (final RemoteIssue issue : issues) {
			boolean found = false;
			for (final UserStory userStory : sprint.getUserStories()) {
				final RemoteReference reference = userStory.getRemoteReferenceForSystemId(this.getSystemId());
				if (reference != null && reference.getRemoteId().equals(issue.getKey())) {
					found = true;
					break;
				}
			}

		}
	}

	@Override
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String getSystemId() {
		return this.systemId;
	}
}
