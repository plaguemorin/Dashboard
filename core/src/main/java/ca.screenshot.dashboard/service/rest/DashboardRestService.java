package dashboard.service.rest;

import dashboard.entity.Sprint;
import dashboard.service.repositories.SprintAPI;
import dashboard.service.rest.vo.Dashboard;
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
@Path("/teams/{teamName}/dashbaord")
public class DashboardRestService {
	@Autowired
	private SprintAPI sprintAPI;

	@GET
	public Dashboard getDashboardForTeam(@PathParam("teamName") final String teamName) {
		final Dashboard dashboard = new Dashboard();
		final Sprint theSprint = this.sprintAPI.getCurrentSprintForTeam(teamName);

		dashboard.setTeamName(theSprint.getTeamName());
		dashboard.setSprintName(theSprint.getSprintKey());

		return dashboard;
	}
}
