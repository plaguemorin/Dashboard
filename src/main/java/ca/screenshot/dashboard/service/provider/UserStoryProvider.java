package ca.screenshot.dashboard.service.provider;

import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;

/**
 * User: plaguemorin
 * Date: 12/07/12
 * Time: 1:15 PM
 */
public interface UserStoryProvider extends RemoteSystem {
	void refreshUserStory(final UserStory userStory);

	void refreshTask(final UserStoryTask task);
}
