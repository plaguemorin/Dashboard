package ca.screenshot.dashboard.service.rest.vo;

import ca.screenshot.dashboard.entity.UserStoryTask;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: plaguemorin
 * Date: 26/07/12
 * Time: 5:05 PM
 */
@XmlRootElement
public class DashboardTask {

	public static DashboardTask fromTask(UserStoryTask task) {
		final DashboardTask dashboardTask = new DashboardTask();

		
		return dashboardTask;
	}
}
