package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.service.repositories.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Collection;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 5:10 PM
 */
@Path("/teams/{teamName}/{sprintName}/userstories/{userStoryGuid}/tasks")
@Service
public class SprintUserStoryTasksRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintUserStoryTasksRestResource.class);

	@Autowired
	private SprintRepository sprintRepository;

	@GET
	public Collection<UserStoryTask> taskList(@PathParam("teamName") String teamName, @PathParam("sprintName") final String sprintName, @PathParam("userStoryGuid") String userStoryGuid) {
		final Sprint sprint = this.sprintRepository.getSprintForTeam(teamName, sprintName);
		final UserStory userStory = sprint.getUserStory(userStoryGuid);
		return userStory.getTasks();
	}


}
