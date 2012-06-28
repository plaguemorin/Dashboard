package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author PLMorin
 *         17/05/12 8:38 AM
 */
@Path("/teams/{teamName}")
@Service
public class SpecificTeamRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpecificTeamRestResource.class);

	@Autowired
	private SprintAPI sprintAPI;

	@GET
	public List<Sprint> getTeamResource(@PathParam("teamName") final String teamName) {
		return this.sprintAPI.getSprintsForTeam(teamName);
	}

}
