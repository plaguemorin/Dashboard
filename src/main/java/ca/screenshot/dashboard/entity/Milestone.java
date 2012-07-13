package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 2:20 AM
 */
@XmlRootElement
@Entity
public class Milestone extends AbstractValueObject {
	@Id
	private String milestoneKey;

	@OneToMany(mappedBy = "milestone")
	private List<UserStory> userStoryList = new ArrayList<>();

	@XmlID
	@XmlAttribute(name = "milestoneId")
	public String getGuid() {
		return this.milestoneKey;
	}

	public void setGuid(String guid) {
		this.milestoneKey = guid;
	}
}
