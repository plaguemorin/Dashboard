package ca.screenshot.dashboard.service.providers;

import ca.screenshot.dashboard.entity.ActivityLog;

import java.util.List;

/**
 * @author plaguemorin
 *         Date: 24/05/12
 *         Time: 12:39 PM
 */
public interface ActivityLogProvider {
	List<ActivityLog> getLog();
}
