package dashboard.service.rest;

import dashboard.entity.Sprint;
import dashboard.entity.UserStoryTask;
import dashboard.service.repositories.SprintAPI;
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
@Path("/teams/{teamName}/{sprintName}/tasks")
@Service
public class SprintTasksRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintTasksRestResource.class);

	@Autowired
	private SprintAPI sprintAPI;

	@GET
	public Collection<UserStoryTask> taskList(@PathParam("teamName") String teamName, @PathParam("sprintName") final String sprintName, @PathParam("userStoryGuid") String userStoryGuid) {
		final Sprint sprint = this.sprintAPI.getSprintByKey(teamName, sprintName);
		final Collection<UserStoryTask> tasks = sprint.getUserStory(userStoryGuid).getTasks();
		tasks.size();
		return tasks;
	}


}
