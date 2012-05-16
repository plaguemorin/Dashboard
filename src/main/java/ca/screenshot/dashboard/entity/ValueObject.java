package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 */
public interface ValueObject {
	@XmlID
	@XmlAttribute(name = "guid", required = true)
	String getGuid();

	void setGuid(final String guid);
}
