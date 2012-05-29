package ca.screenshot.dashboard.entity;

import java.util.Collection;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 1:12 PM
 */
public interface LoggedObject {
	Collection<ActivityLog> getActivityLog();

	void addLog(final ActivityLog activityLog);

	void updateLog(LoggedObject valueObject);
}
