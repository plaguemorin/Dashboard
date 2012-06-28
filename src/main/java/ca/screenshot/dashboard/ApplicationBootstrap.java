package ca.screenshot.dashboard;

import ca.screenshot.dashboard.service.providers.SprintProvider;
import ca.screenshot.dashboard.service.providers.UserStoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:22 PM
 */
@Component
public class ApplicationBootstrap {
	@Autowired
	private UserStoryProvider userStoryProvider;

	@Autowired
	private SprintProvider sprintProvider;

	@PostConstruct
	public void init() {
	}
}
