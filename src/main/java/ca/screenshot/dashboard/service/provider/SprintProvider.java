package ca.screenshot.dashboard.service.provider;

import ca.screenshot.dashboard.entity.Sprint;

/**
 * User: plaguemorin
 * Date: 24/07/12
 * Time: 2:32 PM
 */
public interface SprintProvider extends RemoteSystem {
	void refreshSprint(final Sprint sprint);
}
