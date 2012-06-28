package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Collection;

/**
 * @author plaguemorin
 *         Date: 17/05/12
 *         Time: 10:31 AM
 */
@Path("/teams/{teamName}/{sprintName}/userstories/")
@Service
public class SprintUserStoriesRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintUserStoriesRestResource.class);

	@Autowired
	private SprintAPI sprintAPI;

	@GET
	@Transactional
	public Collection<UserStory> getListOfUserStoriesForSprint(@PathParam("teamName") final String teamName, @PathParam("sprintName") final String sprintName) {
		final Collection<UserStory> userStories = this.sprintAPI.getSprintByKey(teamName, sprintName).getUserStories();

		userStories.size();

		return userStories;
	}


}
