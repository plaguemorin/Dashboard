package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * @author PLMorin
 *         16/05/12 9:00 PM
 */
@Repository
public class SprintAPIImpl implements SprintAPI {
	private final static Logger LOGGER = LoggerFactory.getLogger(SprintAPIImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Sprint getSprintByKey(final String teamName, final String sprintKey) {
		LOGGER.debug("Getting sprint keyed \"" + sprintKey + "\"");

		final TypedQuery<Sprint> query = entityManager.createNamedQuery("Sprint.specificSprint", Sprint.class);
		query.setParameter("sprintKey", sprintKey);
		query.setParameter("teamName", teamName);

		return query.getSingleResult();
	}

	@Override
	@Transactional
	public List<Sprint> getSprintsForTeam(final String teamName) {
		final TypedQuery<Sprint> sprints = entityManager.createNamedQuery("Sprint.allSprintForTeam", Sprint.class);
		sprints.setParameter("teamName", teamName);
		return sprints.getResultList();
	}

	@Override
	public Sprint getCurrentSprintForTeam(final String teamName) {
		return null;
	}

	@Override
	@Transactional
	public Sprint createNewSprintForTeam(final String teamName, final Date startDate) {
		final Sprint newSprint = new Sprint();
		newSprint.setTeamName(teamName);
		newSprint.setStartDate(startDate);

		final List<Sprint> possibleSprints = this.getSprintsForTeam(teamName);
		newSprint.setSprintKey("S" + (possibleSprints.size() + 1));

		LOGGER.info("Created a new sprint for team \"" + teamName + "\" as key: " + newSprint.getSprintKey());

		this.entityManager.persist(newSprint);
		return newSprint;
	}

	@Override
	@Transactional
	public void saveSprint(final Sprint sprint) {
		for (final UserStory userStory : sprint.getUserStories()) {
			if (userStory.getGuid() != null) {
				this.entityManager.merge(userStory);
			} else {
				this.entityManager.persist(userStory);
			}
		}


		this.entityManager.merge(sprint);
	}

}
