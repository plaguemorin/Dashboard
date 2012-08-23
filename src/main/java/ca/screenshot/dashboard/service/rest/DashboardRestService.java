package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import ca.screenshot.dashboard.service.rest.vo.Dashboard;
import ca.screenshot.dashboard.service.rest.vo.DashboardStory;
import ca.screenshot.dashboard.service.rest.vo.DashboardTask;
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
			processUserStory(dashboard, now, userStory);
		}

		dashboard.setGoals(theSprint.getGoals());

		return dashboard;
	}

	private void processUserStory(Dashboard dashboard, Date now, UserStory userStory) {
		for (final UserStoryTask task : userStory.getTasks()) {
			processTask(dashboard, now, task);
		}

		switch (userStory.getCurrentStatus()) {
			case IN_PROGRESS:
				break;
			case DONE:
				dashboard.addReadyToDemoStory(DashboardStory.fromUserStory(userStory));
				break;
			case VERIFY:
				dashboard.addUserStoryToVerify(DashboardStory.fromUserStory(userStory));
				break;
			case TODO:
				break;
		}

		dashboard.addUserStory(DashboardStory.fromUserStory(userStory));
		dashboard.setPoints(dashboard.getPoints() + userStory.getStoryPoints());
	}

	private void processTask(Dashboard dashboard, Date now, UserStoryTask task) {
		// What should finish today ?
		if (task.getStartDate() != null) {
			final Date shouldEndDate = new Date(task.getStartDate().getTime() + task.getSecondsEstimated() + (24 * 60 * 60 * 100));
			if (shouldEndDate.after(now)) {
				LOGGER.info("Task " + task.getKey() + " should end soon");
				dashboard.addEndingSoonTask(DashboardTask.fromTask(task));
			}
		}
	}
}
