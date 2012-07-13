package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.UserStoryTaskWork;
import ca.screenshot.dashboard.service.repositories.UserStoryAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * User: plaguemorin
 * Date: 13/07/12
 * Time: 6:19 PM
 */
@Service
@Path("/stories/{storyKey}/tasks/{taskId}/work/{id}")
public class WorkLogForTaskRestResource {
	@Autowired
	private UserStoryAPI userStoryAPI;

	@GET
	@Transactional(readOnly = true)
	public UserStoryTaskWork get(@PathParam("storyKey") final String storyKey, @PathParam("taskId") final String taskId, @PathParam("id") final String id) {
		for (final UserStoryTaskWork work : this.userStoryAPI.getUserStoryByKey(storyKey).getTask(taskId).getWorkList()) {
			if (work.getWorkLogId().equals(id)) {
				work.getRemoteReferences().size();
				return work;
			}
		}
		return null;
	}
}
