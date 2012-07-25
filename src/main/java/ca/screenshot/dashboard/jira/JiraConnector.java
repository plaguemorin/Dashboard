package ca.screenshot.dashboard.jira;

import ca.screenshot.dashboard.remote.jira.*;
import org.apache.axis.AxisFault;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:16 PM
 */
public class JiraConnector {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraConnector.class);

	private URL jiraUrl;
	private String username;
	private String password;

	private String authKey;
	private JiraSoapService jiraSoapService;

	@PostConstruct
	public void init() throws AxisFault {
		LOGGER.info("Using JIRA url: " + jiraUrl.toExternalForm());

		this.jiraSoapService = new JirasoapserviceV2SoapBindingStub(this.jiraUrl, null);

		this.setupConnection();
		LOGGER.info("Using auth-key: " + authKey);
	}

	@PreDestroy
	public void destory() throws RemoteException {
		if (this.authKey != null) {
			this.jiraSoapService.logout(this.authKey);
		}
	}

	private void setupConnection() {
		// Test Auth Key if we have one
		if (this.authKey != null) {
			try {
				final RemoteServerInfo serverInfo = this.jiraSoapService.getServerInfo(this.authKey);

				LOGGER.info("Connected to JIRA version " + serverInfo.getVersion());
			} catch (RemoteException e) {
				LOGGER.debug("JIRA at \"" + this.jiraUrl + "\" refused the key, regenerating one");
				this.authKey = null;
			}
		}

		// If we have no key, fetch one
		if (this.authKey == null) {
			try {
				LOGGER.debug("Connecting to JIRA at URL \"" + this.jiraUrl + "\" with username: \"" + this.username + "\"");
				this.authKey = jiraSoapService.login(this.username, this.password);
			} catch (RemoteException e) {
				LOGGER.error("Unable to authenticate with JIRA with username \"" + this.username + " \"", e);
			}
		}
	}

	public List<RemoteIssue> getIssuesFromJqlSearch(String jql, int number) {
		this.setupConnection();

		try {
			final List<RemoteIssue> remoteIssues = asList(this.jiraSoapService.getIssuesFromJqlSearch(this.authKey, jql, number));

			if (LOGGER.isDebugEnabled()) {
				for (final RemoteIssue remoteIssue : remoteIssues) {
					for (final RemoteCustomFieldValue customFieldValue : remoteIssue.getCustomFieldValues()) {
						LOGGER.debug("Custom Field Key [" + customFieldValue.getKey() +
											 "], Id [" + customFieldValue.getCustomfieldId() +
											 "], Value [" + ArrayUtils.toString(customFieldValue.getValues()) + "]");
					}
				}
			}

			return remoteIssues;
		} catch (RemoteException e) {
			LOGGER.error("Unable to execute remote JQL query on server \"" + this.jiraUrl + "\"", e);
			throw new IllegalStateException("Remote exception caught", e);
		}
	}

	public List<RemoteVersion> getVersions(String projectName) {
		this.setupConnection();

		try {
			return asList(this.jiraSoapService.getVersions(this.authKey, projectName));
		} catch (RemoteException e) {
			LOGGER.error("Unable to execute remote getVersions on server \"" + this.jiraUrl + "\"", e);
			throw new IllegalStateException("Remote exception caught", e);
		}
	}

	public RemoteUser getUser(String user) {
		if (!isEmpty(user)) {
			this.setupConnection();
			try {
				return this.jiraSoapService.getUser(this.authKey, user);
			} catch (RemoteException e) {
				LOGGER.error("Unable to execute remote getUser on server \"" + this.jiraUrl + "\"", e);
				throw new IllegalStateException("Remote exception caught", e);
			}
		} else {
			throw new IllegalArgumentException("User must not be null !");
		}
	}

	public RemoteIssue getIssue(final String remoteId) {
		this.setupConnection();
		try {
			return this.jiraSoapService.getIssue(this.authKey, remoteId);
		} catch (RemoteException e) {
			LOGGER.error("Unable to execute remote getIssue on server \"" + this.jiraUrl + "\"", e);
			throw new IllegalStateException("Remote exception caught", e);
		}
	}

	public List<RemoteStatus> getRemoteStatus() {
		this.setupConnection();
		try {
			return asList(this.jiraSoapService.getStatuses(this.authKey));
		} catch (RemoteException e) {
			LOGGER.error("Unable to execute remote getStatues on server \"" + this.jiraUrl + "\"", e);
			throw new IllegalStateException("Remote exception caught", e);
		}
	}

	public List<RemoteIssue> getChildIssues(final String parentId) {
		return this.getIssuesFromJqlSearch("parent =\"" + parentId + "\"", 10000);
	}

	public List<RemoteIssue> getUserStoriesForSprint(final String sprintName) {
		return this.getIssuesFromJqlSearch("type =\"User story\" and fixVersion =\"" + sprintName + "\"", 10000);
	}

	public void setJiraUrl(URL jiraUrl) {
		this.jiraUrl = jiraUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
