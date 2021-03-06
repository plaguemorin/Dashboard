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

	UserStory create(Product product, String title, String description, int points, String remoteReference);

	UserStory create(Product product, String title, String description, int points);

	UserStory getUserStoryByKey(String key);

	void remoteUpdate(UserStory story);

	UserStory createRemote(Product product, String remoteReferenceUrl);
}
