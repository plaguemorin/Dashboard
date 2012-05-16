package ca.screenshot.dashboard.service.providers;

import ca.screenshot.dashboard.entity.Sprint;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:39 PM
 */
public interface SprintProvider {
	void findLatestSprint(Sprint sprint);
}
