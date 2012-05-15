package ca.screenshot.dashboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author plaguemorin
 *         Date: 15/05/12
 *         Time: 5:39 PM
 */
@Service
public class JiraImporter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraImporter.class);


	@PostConstruct
	public void init() {

	}
}
