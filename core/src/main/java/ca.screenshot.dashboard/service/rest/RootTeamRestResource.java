package dashboard.service.rest;

import dashboard.service.repositories.SprintAPI;
import dashboard.service.rest.vo.ListOfTeams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static java.util.Arrays.asList;

/**
 * User: PLMorin
 * Date: 16/05/12
 * Time: 8:49 PM
 */
@Path("/teams")
@Service
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class RootTeamRestResource {
	@Autowired
	private SprintAPI sprintProvider;

	@GET
	public ListOfTeams getTeamNames() {
		final ListOfTeams listOfTeams = new ListOfTeams();
		listOfTeams.setTeams(asList("CANADIENS"));

		return listOfTeams;
	}
}
