package ca.screenshot.dashboard.jira;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;

/**
 * User: plaguemorin
 * Date: 01/08/12
 * Time: 4:16 PM
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class JiraHttpConnectorTest {

	@Value("${jira.url}")
	private URL jiraUrl;

	@Value("${jira.userName}")
	private String jiraUsername;

	@Value("${jira.password}")
	private String jiraPassword;

	private JiraHttpConnector connector;

	@Before
	public void init() {
		this.connector = new JiraHttpConnector();
		this.connector.setJiraUrl(this.jiraUrl);
		this.connector.setUsername(this.jiraUsername);
		this.connector.setPassword(this.jiraPassword);

		this.connector.init();
	}

	@Test
	public void test() {
		this.connector.getIssue("CANADIENS-414");
	}
}
