package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Product;

import java.util.Collection;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 1:51 AM
 */
public interface ProductAPI {
	Product create(final String key);

	Collection<Product> listAll();

	Product getByKey(final String productId);
}
