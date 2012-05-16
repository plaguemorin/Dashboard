package ca.screenshot.dashboard.entity;

import ca.screenshot.dashboard.AbstractValueObject;

/**
 */
public class UserStory extends AbstractValueObject {

	private UserStoryStatus storyStatus;
	private String remoteIdentifier;

	public String getRemoteIdentifier() {
		return remoteIdentifier;
	}

	public void setRemoteIdentifier(String remoteIdentifier) {
		this.remoteIdentifier = remoteIdentifier;
	}


}
