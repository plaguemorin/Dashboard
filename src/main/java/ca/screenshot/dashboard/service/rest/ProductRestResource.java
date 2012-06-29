package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Product;
import ca.screenshot.dashboard.service.repositories.ProductAPI;
import ca.screenshot.dashboard.service.rest.vo.ListOfProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Collection;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 11:48 AM
 */
@Service
@Path("/products")
public class ProductRestResource {
	@Autowired
	private ProductAPI productAPI;

	@GET
	@Transactional(readOnly = true)
	public ListOfProducts listAllProducts() {
		final Collection<Product> products = this.productAPI.listAll();
		final ListOfProducts vo = new ListOfProducts();

		vo.setProducts(products);

		return vo;
	}

	@GET
	@Path("/{productId}")
	@Transactional(readOnly = true)
	public Product getProduct(@PathParam("productId") final String productId) {
		final Product product = this.productAPI.getByKey(productId);

		product.getNextUserStoryKey();

		return product;
	}
}
