package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.repositories.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author PLMorin
 *         17/05/12 8:38 AM
 */
@Path("/teams/{teamName}")
@Service
public class SpecificTeamRestResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SpecificTeamRestResource.class);

	@Autowired
	private SprintRepository sprintRepository;

	@GET
	public Sprint getTeamResource(@PathParam("teamName") final String teamName)
	{
		return this.sprintRepository.getSprintForTeam(teamName);
	}
}
