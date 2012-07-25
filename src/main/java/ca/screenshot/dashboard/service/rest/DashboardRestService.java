package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import ca.screenshot.dashboard.service.rest.vo.Dashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Date;

/**
 * @author plaguemorin
 *         Date: 17/05/12
 *         Time: 11:02 AM
 */
@Service
@Path("/teams/{teamName}/dashboard")
public class DashboardRestService {
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardRestService.class);

	@Autowired
	private SprintAPI sprintAPI;

	@GET
	@Transactional(readOnly = true)
	public Dashboard getDashboardForTeam(@PathParam("teamName") final String teamName) {
		final Dashboard dashboard = new Dashboard();
		final Sprint theSprint = this.sprintAPI.getCurrentSprintForTeam(teamName);
		final Date now = new Date();

		dashboard.setTeamName(theSprint.getTeamName());
		dashboard.setSprintName(theSprint.getSprintKey());

		for (final UserStory userStory : theSprint.getUserStories()) {
			for (final UserStoryTask task : userStory.getTasks()) {
					// What should finish today ?
				if (task.isStarted() && !task.isDone()) {
					final Date shouldEndDate = new Date(task.getStartDate().getTime() + task.getSecondsEstimated() + (24 * 60 * 60 * 100));
					if (shouldEndDate.after(now)) {
						LOGGER.info("Task " + task.getKey() + " is done");
						dashboard.addEndingSoonTask(task);
					}
				}
			}

			dashboard.setPoints(dashboard.getPoints() + userStory.getStoryPoints());
			dashboard.addUserStory(userStory);
		}

		dashboard.setGoals(theSprint.getGoals());


		return dashboard;
	}
}
