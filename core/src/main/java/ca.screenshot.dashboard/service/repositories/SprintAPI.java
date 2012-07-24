package dashboard.service.repositories;

import dashboard.entity.Sprint;

import java.util.Date;
import java.util.List;

/**
 * Interface for sprint management
 *
 * @author PLMorin
 *         16/05/12 8:58 PM
 */
public interface SprintAPI {
	/**
	 * Get a sprint for a team by it's key
	 *
	 * @param teamName  the team name
	 * @param sprintKey the sprint key
	 * @return the sprint
	 */
	Sprint getSprintByKey(final String teamName, final String sprintKey);

	/**
	 * Returns a list of all sprints that are possible for a team
	 *
	 * @param teamName the team to lists the sprints from
	 * @return list of all sprints for a team
	 */
	List<Sprint> getSprintsForTeam(String teamName);

	/**
	 * By some logic, figure out what the current sprint is and return it
	 *
	 * @param teamName the team to get the latest sprint
	 * @return the current sprint
	 */
	Sprint getCurrentSprintForTeam(String teamName);

	/**
	 * Creates a sprint for a team with default name
	 *
	 * @param teamName  the team name to get the new sprint
	 * @param startDate when the sprint starts
	 * @return the new sprint
	 */
	Sprint createNewSprintForTeam(String teamName, Date startDate);

	void saveSprint(final Sprint sprint);
}
