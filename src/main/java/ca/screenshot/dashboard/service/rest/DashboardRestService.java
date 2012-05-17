package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.repositories.SprintRepository;
import ca.screenshot.dashboard.service.rest.vo.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author plaguemorin
 *         Date: 17/05/12
 *         Time: 11:02 AM
 */
@Service
@Path("/dashboard")
public class DashboardRestService {
	@Autowired
	private SprintRepository sprintRepository;

	@GET
	@Path("/{teamName}")
	public Dashboard getDashboardForTeam(@PathParam("teamName") final String teamName) {
		final Dashboard dashboard = new Dashboard();
		final Sprint theSprint = this.sprintRepository.getSprintForTeam(teamName);

		dashboard.setTeamName(theSprint.getTeamName());
		dashboard.setSprintName(theSprint.getSprintName());

		return dashboard;
	}
}
