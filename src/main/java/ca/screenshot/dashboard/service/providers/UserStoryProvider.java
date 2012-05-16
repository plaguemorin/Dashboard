package ca.screenshot.dashboard.service.providers;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;

import java.util.List;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:10 PM
 */
public interface UserStoryProvider {
	List<UserStory> getUserStoriesForSprint(final Sprint sprint);
}
