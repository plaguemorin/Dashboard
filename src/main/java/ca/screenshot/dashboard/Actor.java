package ca.screenshot.dashboard;

import javax.xml.bind.annotation.XmlRootElement;

/**
 */
@XmlRootElement
public class Actor extends AbstractValueObject {
	private String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
