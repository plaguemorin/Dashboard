package ca.screenshot.dashboard.service;

import ca.screenshot.dashboard.entity.Sprint;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:10 PM
 */
public interface UserStoryImporter {

	void updateUserStories(final Sprint sprint);

	void findLatestSprint(Sprint sprint);
}
