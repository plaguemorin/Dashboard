package ca.screenshot.dashboard;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.Role;
import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.service.repositories.ParticipantAPI;
import ca.screenshot.dashboard.service.repositories.SprintAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:22 PM
 */
@Component
public class ApplicationBootstrap {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationBootstrap.class);

	@Autowired
	private SprintAPI sprintAPI;

	@Autowired
	private ParticipantAPI participantAPI;

	@PostConstruct
	@Transactional(propagation = REQUIRES_NEW)
	public void init() {
		LOGGER.info("Application bootstrapping");
		final Sprint sprint = sprintAPI.createNewSprintForTeam("CANADIENS", Calendar.getInstance().getTime());

		sprint.setWorkDays(10);

		final UserStory userStory = new UserStory();
		userStory.setTitle("Update the inventory model");
		userStory.setDescription("We need to update the inventory model to allow future inventory");
		userStory.setStoryPoints(8);
		sprint.addUserStory(userStory);

		final Participant me = participantAPI.create("philippe.lague-morin@hybris.com");
		me.setDisplayName("Philippe Lague-Morin");
		me.setHoursPerDay(8);
		me.setEmail(me.getUser());
		this.participantAPI.save(me);

		sprint.addParticipant(me, Role.DEVELOPER);

		sprintAPI.saveSprint(sprint);
		LOGGER.info("Application bootstrapping done");
	}
}
