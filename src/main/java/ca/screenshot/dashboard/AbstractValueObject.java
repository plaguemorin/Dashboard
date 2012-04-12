package ca.screenshot.dashboard;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 */
public abstract class AbstractValueObject implements ValueObject, Serializable {
	@XmlAttribute(name = "guid", required = true)
	private String guid;


	@Override
	public String getGuid() {
		return this.guid;
	}

	@Override
	public void setGuid(String guid) {
		this.guid = guid;
	}
}
