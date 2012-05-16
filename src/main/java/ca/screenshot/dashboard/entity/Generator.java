package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 */
@XmlRootElement
public class Generator extends AbstractValueObject {
	private String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
