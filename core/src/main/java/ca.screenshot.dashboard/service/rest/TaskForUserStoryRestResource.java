package dashboard.service.rest;

import dashboard.entity.UserStoryTask;
import dashboard.service.repositories.UserStoryAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * User: plaguemorin
 * Date: 10/07/12
 * Time: 8:41 PM
 */
@Path("/stories/{storyKey}/tasks/{taskId}")
@Service
public class TaskForUserStoryRestResource {
	@Autowired
	private UserStoryAPI userStoryAPI;

	@GET
	@Transactional(readOnly = true)
	public UserStoryTask getUST(@PathParam("storyKey") final String storyKey, @PathParam("taskId") final String taskId) {
		return this.userStoryAPI.getUserStoryByKey(storyKey).getTask(taskId);
	}
}
