package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Product;
import ca.screenshot.dashboard.entity.UserStory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 1:55 AM
 */
@Repository
public class UserStoryAPIImpl implements UserStoryAPI {
	private final static Logger LOGGER = LoggerFactory.getLogger(UserStoryAPIImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public UserStory create(final Product product) {
		final UserStory userStory = new UserStory();
		userStory.setStoryKey(product.getNextUserStoryKey());
		product.addUserStory(userStory);

		entityManager.persist(userStory);

		return userStory;
	}

	@Override
	@Transactional
	public void save(final UserStory userStory) {
		this.entityManager.merge(userStory);
	}
}
