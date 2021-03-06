package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.service.repositories.UserStoryAPI;
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
		final UserStoryTask task = this.userStoryAPI.getUserStoryByKey(storyKey).getTask(taskId);
		task.getRemoteReferences().size();
		task.getWorkList().size();
		return task;
	}
}
