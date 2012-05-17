package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Sprint;

import java.util.List;

/**
 * @author PLMorin
 *         16/05/12 8:58 PM
 */
public interface SprintRepository {
	Sprint getSprintForTeam(String teamName);

	List<Sprint> getPossibleSprints(String teamName);
}
