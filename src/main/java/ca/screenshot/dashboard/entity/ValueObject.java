package ca.screenshot.dashboard.entity;

import java.util.Date;

/**
 */
public interface ValueObject {
	String getGuid();

	void setGuid(final String guid);

	Date getModifiedDate();
}
