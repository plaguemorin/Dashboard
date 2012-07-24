package dashboard.service.rest.vo;

import dashboard.entity.Product;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 11:52 AM
 */
@XmlRootElement(name = "listOfProducts")
public class ListOfProducts {
	private Collection<Product> products;

	@XmlIDREF
	@XmlElementWrapper(name = "products")
	@XmlElement(name = "product")
	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}
}
