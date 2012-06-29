package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.Sprint;
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
 *         Time: 10:50 AM
 */
@Path("/teams/{teamName}/{sprintKey}/userstories/{userStoryGuid}")
@Service
public class SprintUserStoryRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintUserStoryRestResource.class);

	@Autowired
	private SprintAPI sprintAPI;

	@GET
	@Transactional(readOnly = true)
	public UserStory get(@PathParam("teamName") String teamName, @PathParam("sprintKey") final String sprintName, @PathParam("userStoryGuid") String userStoryGuid) {
		final Sprint sprint = this.sprintAPI.getSprintByKey(teamName, sprintName);
		final UserStory userStory = sprint.getUserStory(userStoryGuid);
		userStory.getTasks().size();
		return userStory;
	}

	@Path("/participants")
	@GET
	@Transactional(readOnly = true)
	public Collection<Participant> getParticipantsForStory(@PathParam("teamName") String teamName, @PathParam("sprintName") final String sprintName, @PathParam("userStoryGuid") String userStoryGuid) {
		final UserStory userStory = this.sprintAPI.getSprintByKey(teamName, sprintName).getUserStory(userStoryGuid);
		return userStory.getParticipants();
	}
}
