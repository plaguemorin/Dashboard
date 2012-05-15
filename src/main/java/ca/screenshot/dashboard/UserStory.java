package ca.screenshot.dashboard;

/**
 */
public class UserStory extends AbstractValueObject {

	public enum Status {
		TODO,
		IN_PROGRESS,
		DONE
	}

	private Status storyStatus;
	private String guid;

}
