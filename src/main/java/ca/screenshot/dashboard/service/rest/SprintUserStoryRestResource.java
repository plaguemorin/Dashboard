package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.service.repositories.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Collection;

/**
 * @author plaguemorin
 *         Date: 17/05/12
 *         Time: 10:50 AM
 */
@Path("/teams/{teamName}/userstories/{userStoryGuid}")
@Service
@Scope("prototype")
public class SprintUserStoryRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintUserStoryRestResource.class);

	@Autowired
	private SprintRepository sprintRepository;

	@PathParam("teamName")
	private String teamName;

	@PathParam("userStoryGuid")
	private String userStoryGuid;

	@GET
	public UserStory get() {
		return this.sprintRepository.getSprintForTeam(teamName).getUserStory(userStoryGuid);
	}

	@Path("/participants")
	@GET
	public Collection<Participant> getParticipantsForStory() {
		final UserStory userStory = this.sprintRepository.getSprintForTeam(teamName).getUserStory(userStoryGuid);
		return userStory.getParticipants();
	}
}
