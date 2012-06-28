package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Date;

/**
 *
 */
@Path("/teams/{teamName}/{sprintName}")
@Service
public class SprintRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintRestResource.class);

	@Autowired
	private SprintAPI sprintAPI;

	@GET
	@Transactional
	public Sprint getSprint(@PathParam("teamName") final String teamName, @PathParam("sprintName") final String sprintName) {
		final Sprint sprint = this.sprintAPI.getSprintByKey(teamName, sprintName);

		sprint.getUserStories().size();
		sprint.getParticipants();

		return sprint;
	}

	@POST
	public Sprint createSprint(@PathParam("teamName") final String teamName, @PathParam("sprintName") final String sprintName) {
		return this.sprintAPI.createNewSprintForTeam(teamName, new Date());
	}
}
