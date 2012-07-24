package entity;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 1:51 AM
 */
@Entity
@XmlRootElement
@NamedQueries({
					  @NamedQuery(name = "Product.All", query = "SELECT a FROM Product a"),
					  @NamedQuery(name = "Product.ByKey", query = "SELECT a FROM Product a WHERE a.productKey = :key")
})
public class Product extends AbstractValueObject {
	@Id
	private String productKey;

	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "product")
	private List<UserStory> userStories = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlID
	public String getKey() {
		return productKey;
	}

	public void setKey(final String key) {
		this.productKey = key;
	}


	public void addUserStory(final UserStory userStory) {
		this.userStories.add(userStory);
		userStory.setProduct(this);
	}

	@XmlIDREF
	@XmlElementWrapper(name = "stories")
	@XmlElement(name = "story")
	public Collection<UserStory> getUserStories() {
		return this.userStories;
	}

	@XmlElement(name = "next-story-key")
	public String getNextUserStoryKey() {
		return this.getKey() + "-" + (this.userStories.size() + 1);
	}
}
