package dashboard.service.repositories;

import dashboard.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

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

	@Override
	@Transactional(readOnly = true)
	public Collection<Product> listAll() {
		final TypedQuery<Product> query = this.entityManager.createNamedQuery("Product.All", Product.class);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Product getByKey(String productId) {
		final TypedQuery<Product> query = this.entityManager.createNamedQuery("Product.ByKey", Product.class);
		query.setParameter("key", productId);
		return query.getSingleResult();
	}
}
