package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStoryTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

/**
 * @author PLMorin
 *         16/05/12 9:00 PM
 */
@Repository
public class SprintRepositoryImpl implements SprintRepository {
	private final static Logger LOGGER = LoggerFactory.getLogger(SprintRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Sprint getSprintForTeam(final String teamName, final String sprintName) {
		final TypedQuery<Sprint> query = entityManager.createNamedQuery("Sprint.specificSprint", Sprint.class);
		query.setParameter("teamName", teamName);
		query.setParameter("sprintName", sprintName);
		return query.getSingleResult();
	}

	@Override
	@Transactional
	public List<Sprint> getPossibleSprints(final String teamName) {
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
	public Collection<UserStoryTask> getTasksForUserStoryInSprint(final String teamName, final String sprintName, final String userStoryGuid) {
		final Collection<UserStoryTask> tasks = this.getSprintForTeam(teamName, sprintName).getUserStory(userStoryGuid).getTasks();
		tasks.size();
		return tasks;
	}

	@Override
	public Sprint createNewSprintForTeamWithName(final String teamName, final String sprintName) {
		final Sprint newSprint = new Sprint();
		newSprint.setTeamName(teamName);
		newSprint.setSprintName(sprintName);

		this.entityManager.persist(newSprint);

		return newSprint;
	}

	@Override
	public void saveSprint(final Sprint sprint) {
		this.entityManager.merge(sprint);
	}

}
