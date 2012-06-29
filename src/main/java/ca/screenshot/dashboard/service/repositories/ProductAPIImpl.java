package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 1:59 AM
 */
@Repository
public class ProductAPIImpl implements ProductAPI {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProductAPIImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Product create(String key) {
		final Product product = new Product();
		product.setKey(key);

		this.entityManager.persist(product);

		return product;
	}
}
