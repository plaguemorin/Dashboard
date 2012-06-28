package ca.screenshot.dashboard;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.repositories.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:22 PM
 */
@Component
public class ApplicationBootstrap {

	@Autowired
	private SprintRepository sprintRepository;


	@PostConstruct
	@Transactional
	public void init() {
		final Sprint sprint = sprintRepository.createNewSprintForTeamWithName("CANADIENS", "Sprint 8");

		sprint.setWorkDays(10);
		sprint.setStartDate(Calendar.getInstance());

		sprintRepository.saveSprint(sprint);
	}
}
