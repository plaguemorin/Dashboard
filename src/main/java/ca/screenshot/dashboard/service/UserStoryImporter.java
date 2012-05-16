package ca.screenshot.dashboard.service;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;

import java.util.List;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:10 PM
 */
public interface UserStoryImporter {

	List<UserStory> downloadUserStories(final Sprint sprint);

	void findLatestSprint(Sprint sprint);
}
