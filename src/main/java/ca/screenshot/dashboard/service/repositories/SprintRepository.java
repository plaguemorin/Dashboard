package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStoryTask;

import java.util.Collection;
import java.util.List;

/**
 * @author PLMorin
 *         16/05/12 8:58 PM
 */
public interface SprintRepository {
	Sprint getSprintForTeam(String teamName, String sprintName);

	List<Sprint> getPossibleSprints(String teamName);

	Sprint getCurrentSprintForTeam(String teamName);

	Collection<UserStoryTask> getTasksForUserStoryInSprint(String teamName, String sprintName, String userStoryGuid);

	Sprint createNewSprintForTeamWithName(String teamName, String sprintName);

	void saveSprint(final Sprint sprint);
}
