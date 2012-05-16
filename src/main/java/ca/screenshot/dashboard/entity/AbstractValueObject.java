package ca.screenshot.dashboard.entity;

import java.io.Serializable;

/**
 */
public abstract class AbstractValueObject implements ValueObject, Serializable {
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
