package ca.screenshot.dashboard;

import ca.screenshot.dashboard.entity.*;
import ca.screenshot.dashboard.service.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.GregorianCalendar;

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

	@Autowired
	private UserStoryAPI userStoryAPI;

	@Autowired
	private ProductAPI productAPI;

	@Autowired
	private MilestoneAPI milestoneAPI;

	@PostConstruct
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void init() {
		LOGGER.info("Application bootstrapping");
		final Sprint sprint = sprintAPI.createNewSprintForTeam("CANADIENS", new GregorianCalendar(2012, 7, 9, 0, 0, 0).getTime());
		sprint.setWorkDays(10);

		final Product product = productAPI.create("OMS");
		final Participant participant = participantAPI.create("philippe.lague-morin@hybris.com", "Philippe Lague-Morin", 6);

		final UserStory story91 = this.userStoryAPI.create(product, "[91] Request ATS for SKU(s) and Location(s)", "", 8, "jira-hybris:CANADIENS-388");
		story91.requestRemoteRefresh();
		this.userStoryAPI.save(story91);
		this.userStoryAPI.remoteUpdate(story91);
		sprint.addUserStory(story91);

		final UserStory story88 = this.userStoryAPI.create(product, "[88] Order Listing interface in OMS Cockpit", "", 8);
		story88.addTask("CANADIENS-395");
		story88.addTask("Testing: 1. VO <--> REST Call 2. Service <--> REST 3. Sorting 4. Service get orderlineqty by location", 4 * 60 * 60);
		story88.addTask("Service for getting OLQ based on location", 4 * 60 * 60);
		story88.addTask("REST call to request orderlinequantity per location", 4 * 60 * 60);
		story88.addTask("VO Summary of REST call to list line items", 4 * 60 * 60);
		story88.addTask("UI", 4 * 60 * 60);
		story88.addTask("Add ship method", 4 * 60 * 60);
		this.userStoryAPI.save(story88);
		sprint.addUserStory(story88);

		final UserStory story66 = this.userStoryAPI.create(product, "[66] Sequencing of sourcing algorithms", "", 5);
		story66.addTask("Service: Save/Update/Etc + Dozer", 4 * 60 * 60);
		story66.addTask("Service to integrate a sequence to chose a strategy when there's a tie", 4 * 60 * 60);
		story66.addTask("Modeling: new table", 4 * 60 * 60);
		story66.addTask("Testing", 4 * 60 * 60);
		story66.addTask("UI (Drag and drop)", 4 * 60 * 60);
		story66.addTask("UI Research", 4 * 60 * 60);
		this.userStoryAPI.save(story66);
		sprint.addUserStory(story66);

		final UserStory story65 = this.userStoryAPI.create(product, "[65] Sourcing Algorithm: Item Grouping", "", 5);
		story65.addTask("Applying Algorithm", 4 * 60 * 60);
		story65.addTask("Testing/Demo Widget Demo Data demo", 4 * 60 * 60);
		story65.addTask("Add in the tenant preference tables", 4 * 60 * 60);
		story65.addTask("Testing", 4 * 60 * 60);
		story65.addTask("A new algorithm", 4 * 60 * 60);
		final UserStoryTask task = story65.addTask("Service to get list of location in the DB", 4 * 60 * 60);

		task.addWorkLog(participant, 600L);
		this.userStoryAPI.save(story65);
		sprint.addUserStory(story65);


		sprint.addParticipant(participant, Role.SCRUMMASTER);

		sprintAPI.saveSprint(sprint);
		LOGGER.info("Application bootstrapping done");
	}
}
