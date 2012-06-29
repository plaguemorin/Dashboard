package ca.screenshot.dashboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 1:51 AM
 */
@Entity
@XmlRootElement
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

	public String getNextUserStoryKey() {
		return this.getKey() + "-" + (this.userStories.size() + 1);
	}
}
