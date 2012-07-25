package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Product;
import ca.screenshot.dashboard.entity.RemoteReference;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.service.provider.UserStoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Autowired
	private List<UserStoryProvider> userStoryProviders;

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

	@Override
	@Transactional
	public UserStory create(Product product, String title, String description, int points, String remoteReference) {
		final UserStory u = this.create(product, title, description, points);

		u.addRemoteReference(remoteReference);

		return u;
	}

	@Override
	@Transactional
	public UserStory create(Product product, String title, String description, int points) {
		final UserStory u = this.create(product);

		u.setTitle(title);
		u.setDescription(description);
		u.setStoryPoints(points);

		return u;
	}

	@Override
	@Transactional(readOnly = true)
	public UserStory getUserStoryByKey(String key) {
		final TypedQuery<UserStory> query = this.entityManager.createNamedQuery("UserStories.ByKey", UserStory.class);
		query.setParameter("key", key);
		return query.getSingleResult();
	}

	@Override
	@Transactional
	public void remoteUpdate(final UserStory story) {
		LOGGER.info("Updating " + story.getStoryKey());

		final Map<String, UserStoryProvider> providerMap = generateUserstoryProviderMap();

		for (final RemoteReference reference : story.getRemoteReferences()) {
			if (reference.isDirty()) {
				LOGGER.debug("Remote reference to system " + reference.getSystemId() + " is dirty");
				providerMap.get(reference.getSystemId()).refreshUserStory(story);
			}
		}

		for (final UserStoryTask task : story.getTasks()) {
			LOGGER.debug("Updating task " + task.getTaskId());

			for (final RemoteReference reference : task.getRemoteReferences()) {
				if (providerMap.containsKey(reference.getSystemId())) {
					if (reference.isDirty()) {
						LOGGER.debug("Remote reference to system " + reference.getSystemId() + " is dirty");
						providerMap.get(reference.getSystemId()).refreshTask(task);
					}
				} else {
					LOGGER.warn("User story task " + task.getTaskId() + " has invalid reference to systemId " + reference.getSystemId());
				}
			}
		}

		this.save(story);
	}

	@Override
	@Transactional
	public UserStory createRemote(Product product, String remoteReferenceUrl) {
		final UserStory userStory = this.create(product);
		final RemoteReference reference = userStory.addRemoteReference(remoteReferenceUrl);
		reference.dirty();

		this.remoteUpdate(userStory);

		return userStory;
	}

	/**
	 * Helper function
	 *
	 * @return a map &lt;systemid, reference&gt; of all user stories provider in the system
	 */
	private Map<String, UserStoryProvider> generateUserstoryProviderMap() {
		final Map<String, UserStoryProvider> providerMap = new HashMap<>();
		for (final UserStoryProvider provider : this.userStoryProviders) {
			providerMap.put(provider.getSystemId(), provider);
		}
		return providerMap;
	}
}
