package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.service.repositories.UserStoryAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author plaguemorin
 *         Date: 17/05/12
 *         Time: 10:50 AM
 */
@Path("/stories/{storyKey}")
@Service
public class UserStoryRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserStoryRestResource.class);

	@Autowired
	private UserStoryAPI userStoryAPI;

	@GET
	@Transactional(readOnly = true)
	public UserStory get(@PathParam("storyKey") String key) {
		final UserStory userStory = userStoryAPI.getUserStoryByKey(key);
		userStory.getTasks().size();
		userStory.getRemoteReferences().size();
		return userStory;
	}
}
