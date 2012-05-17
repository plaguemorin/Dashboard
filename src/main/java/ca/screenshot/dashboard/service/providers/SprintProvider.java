package ca.screenshot.dashboard.service.providers;

import ca.screenshot.dashboard.entity.Sprint;

import java.util.List;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:39 PM
 */
public interface SprintProvider
{
	Sprint findLatestSprint(String teamName);

	List<Sprint> findPossibleSprints(String teamName);
}
