package ca.screenshot.dashboard.jira;

import ca.screenshot.dashboard.entity.RemoteReference;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.remote.jira.RemoteCustomFieldValue;
import ca.screenshot.dashboard.remote.jira.RemoteIssue;
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
import java.util.List;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Component
public class JiraProvider implements UserStoryProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraProvider.class);

	private JiraConnector jiraConnector;

	@Value("${jira.customField.storyPoints}")
	private String customFieldIdStoryPoints;

	@Value("${jira.customField.estimatedSeconds}")
	private String customFieldIdEstimatedSeconds;

	@Value("${jira.systemId}")
	private String systemId;

	@Value("${jira.url}")
	private URL jiraUrl;

	@Value("${jira.userName}")
	private String jiraUsername;

	@Value("${jira.password}")
	private String jiraPassword;

	@PostConstruct
	public void init() throws AxisFault {
		this.jiraConnector = new JiraConnector();

		this.jiraConnector.setJiraUrl(this.jiraUrl);
		this.jiraConnector.setUsername(this.jiraUsername);
		this.jiraConnector.setPassword(jiraPassword);

		this.jiraConnector.init();
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
				//userStory.addOrUpdateParticipant(this.participantAPI.findParticipantByUser(subIssue.getAssignee()));
			}
		}

		remoteReference.clean();
	}

	@Override
	public void refreshTask(final UserStoryTask task) {
		final RemoteReference remoteReference = task.getRemoteReferenceForSystemId(this.getSystemId());
		LOGGER.info("Update info for task " + remoteReference.getRemoteId());

		final RemoteIssue issue = this.jiraConnector.getIssue(remoteReference.getRemoteId());
		task.setTitle(issue.getSummary());
		task.setSecondsEstimated(this.extractEstimatedSeconds(issue.getCustomFieldValues()));

		remoteReference.clean();
	}

	private UserStoryTask convertToTask(final RemoteIssue subIssue) {
		final UserStoryTask task = new UserStoryTask();

		task.setTitle(subIssue.getSummary());
		task.addRemoteReference(this.getSystemId(), subIssue.getKey());
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

	@Override
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String getSystemId() {
		return this.systemId;
	}
}
