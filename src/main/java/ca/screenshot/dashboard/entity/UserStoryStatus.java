package ca.screenshot.dashboard.entity;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:55 PM
 */
public enum UserStoryStatus {
	TODO(0),
	IN_PROGRESS(1),
	VERIFY(2), DONE(3);

	private final int weight;

	UserStoryStatus(final int i) {
		this.weight = i;
	}

	public int getWeight() {
		return this.weight;
	}

	public static UserStoryStatus fromWeight(final int w) {
		for (final UserStoryStatus userStoryStatus : UserStoryStatus.values()) {
			if (w == userStoryStatus.weight) {
				return userStoryStatus;
			}
		}

		throw new IllegalArgumentException("Invalid weight argument");
	}
}
