package ca.screenshot.dashboard.service.providers;

import ca.screenshot.dashboard.entity.UserStory;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:10 PM
 */
public interface UserStoryProvider {
	void augmentUserStory(UserStory theSprint);
}
