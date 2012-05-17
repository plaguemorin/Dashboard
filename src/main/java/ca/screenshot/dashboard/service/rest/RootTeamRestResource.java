package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.repositories.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * User: PLMorin
 * Date: 16/05/12
 * Time: 8:49 PM
 */
@Path("/teams")
@Service
public class RootTeamRestResource
{
	@Autowired
	private SprintRepository sprintProvider;

	@GET
	public Sprint getCurrentSprint()
	{
		return this.sprintProvider.getSprintForTeam("CANADIENS");
	}
}
