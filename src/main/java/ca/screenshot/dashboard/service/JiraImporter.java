package ca.screenshot.dashboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Service
public class JiraImporter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraImporter.class);

	@Value("${jiraImporter.jiraUrl}")
	private URL jiraUrl;

	@PostConstruct
	public void init() {
		LOGGER.info("Using JIRA url: " + jiraUrl.toExternalForm());
	}
}
