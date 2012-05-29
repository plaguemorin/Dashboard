package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 */
@XmlRootElement
@Entity
public class Generator extends AbstractValueObject {
	private String displayName;

	private String id;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@XmlID
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
