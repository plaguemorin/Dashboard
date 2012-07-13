package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.Role;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import ca.screenshot.dashboard.service.rest.vo.ParicipantRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author plaguemorin
 *         Date: 17/05/12
 *         Time: 10:30 AM
 */
@Path("/teams/{teamName}/{sprintName}/participants/")
@Service
public class SprintParticipantRestResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpecificTeamRestResource.class);

	@Autowired
	private SprintAPI sprintAPI;

	@GET
	public Collection<ParicipantRole> getListOfParticipantsForSprint(@PathParam("teamName") final String teamName, @PathParam("sprintName") final String sprintName) {
		final Map<Participant, Role> participants = this.sprintAPI.getSprintByKey(teamName, sprintName).getParticipants();
		final List<ParicipantRole> vos = new ArrayList<>();

		for (Map.Entry<Participant, Role> participantRoleEntry : participants.entrySet()) {
			final ParicipantRole paricipantRole = new ParicipantRole();
			paricipantRole.setRole(participantRoleEntry.getValue());
			paricipantRole.setParticipant(participantRoleEntry.getKey());

			vos.add(paricipantRole);
		}

		return vos;
	}
}
