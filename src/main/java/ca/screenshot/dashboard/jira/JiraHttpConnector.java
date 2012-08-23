package ca.screenshot.dashboard.jira;

import ca.screenshot.dashboard.remote.jira.RemoteIssue;
import ca.screenshot.dashboard.remote.jira.RemoteStatus;
import ca.screenshot.dashboard.remote.jira.RemoteUser;
import ca.screenshot.dashboard.remote.jira.RemoteVersion;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

/**
 * User: plaguemorin
 * Date: 01/08/12
 * Time: 11:42 AM
 */
public class JiraHttpConnector {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraHttpConnector.class);

	private String jiraPassword;
	private String jiraUsername;
	private URL jiraUrl;

	private HttpClient httpClient;
	private DocumentBuilder builder;

	public void init() {
		this.httpClient = new HttpClient();

		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			this.builder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.error("Error", e);
		}
	}

	private Document fetchContent(final String path, final String... params) {
		//
		final String replacedPath = doPathReplacement(path, params);
		final String pair = this.jiraUsername + ":" + this.jiraPassword;

		try {
			final URL finalUrl = new URL(this.jiraUrl.getProtocol(), this.jiraUrl.getHost(), this.jiraUrl.getPort(), replacedPath);
			LOGGER.debug("Connecting to URL: {}", finalUrl);

			final GetMethod getMethod = new GetMethod(finalUrl.toString());
			getMethod.addRequestHeader("Authorization", "Basic " + new String(encodeBase64(pair.getBytes())));

			this.httpClient.executeMethod(getMethod);

			if (getMethod.getStatusCode() != 200) {
				throw new IllegalStateException("Unexpected HTTP return code: " + getMethod.getStatusCode() + " with message: " + getMethod.getResponseBodyAsString());
			}

			return builder.parse(getMethod.getResponseBodyAsStream());
		} catch (final IOException e) {
			LOGGER.error("Error connecting to JIRA", e);
			throw new IllegalArgumentException("Unable to fetch with given arguments", e);
		} catch (final SAXException e) {
			LOGGER.error("Error", e);
			throw new IllegalArgumentException("Unable to fetch with given arguments", e);
		}
	}

	private String doPathReplacement(final String path, final String[] params) {
		String replacedPath = path;
		try {
			int i = 0;
			while (replacedPath.contains("{}")) {
				replacedPath = replacedPath.replaceFirst("\\{}", URLEncoder.encode(params[i++], "UTF-8"));
			}
		} catch (final UnsupportedEncodingException e) {
			LOGGER.error("Unsupported Encoding Exception", e);
		}
		return replacedPath;
	}

	public List<RemoteIssue> getIssuesFromJqlSearch(String jql, int number) {
		final Document retXml = this.fetchContent("/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery={}&tempMax={}", jql, String.valueOf(number));
		final NodeList item = retXml.getElementsByTagName("item");

		if (item.getLength() != 1) {
			throw new IllegalArgumentException("");
		}

		final Node node = item.item(0);

		return null;
	}

	public List<RemoteVersion> getVersions(String projectName) {
		return null;
	}

	public RemoteUser getUser(String user) {
		return null;
	}

	public RemoteIssue getIssue(String remoteId) {
		this.getIssuesFromJqlSearch("key =\"" + remoteId + "\"", 10000);

		return null;
	}

	public List<RemoteStatus> getRemoteStatus() {
		return null;
	}

	public List<RemoteIssue> getChildIssues(String parentId) {
		return this.getIssuesFromJqlSearch("parent =\"" + parentId + "\"", 10000);
	}

	public List<RemoteIssue> getUserStoriesForSprint(String sprintName) {
		return null;
	}

	public void setJiraUrl(URL jiraUrl) {
		this.jiraUrl = jiraUrl;
	}

	public void setUsername(String username) {
		this.jiraUsername = username;
	}

	public void setPassword(String password) {
		this.jiraPassword = password;
	}

	public void destroy() {
	}
}
