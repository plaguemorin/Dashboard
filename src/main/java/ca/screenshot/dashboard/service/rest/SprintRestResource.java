package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.repositories.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 */
@Path("/teams/{teamName}/{sprintName}")
@Service
public class SprintRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintRestResource.class);

	@Autowired
	private SprintRepository sprintRepository;

	@GET
	public Sprint getSprint(@PathParam("teamName") final String teamName, @PathParam("sprintName") final String sprintName) {
		return this.sprintRepository.getSprintForTeam(teamName, sprintName);
	}

	@POST
	public Sprint createSprint(@PathParam("teamName") final String teamName, @PathParam("sprintName") final String sprintName) {
		return this.sprintRepository.createNewSprintForTeamWithName(teamName, sprintName);
	}
}
