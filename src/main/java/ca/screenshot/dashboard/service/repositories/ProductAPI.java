package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Product;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 1:51 AM
 */
public interface ProductAPI {
	Product create(final String key);
}
