package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Product;
import ca.screenshot.dashboard.entity.UserStory;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 1:44 AM
 */
public interface UserStoryAPI {
	UserStory create(final Product product);

	void save(UserStory userStory);

	UserStory getUserStoryByKey(String key);
}
