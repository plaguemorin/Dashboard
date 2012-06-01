package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.service.providers.SprintProvider;
import ca.screenshot.dashboard.service.providers.UserStoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * @author PLMorin
 *         16/05/12 9:00 PM
 */
@Repository
public class SprintRepositoryImpl implements SprintRepository {
	private final static Logger LOGGER = LoggerFactory.getLogger(SprintRepositoryImpl.class);

	@Autowired
	private List<SprintProvider> sprintProviders;

	@Autowired
	private List<UserStoryProvider> userStoryProviders;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Sprint getSprintForTeam(String teamName, String sprintName) {
		final Query query = entityManager.createNamedQuery("Sprint.specificSprint");
		query.setParameter("teamName", teamName);
		query.setParameter("sprintName", sprintName);
		final List<Sprint> sprints = query.getResultList();

		if (sprints.isEmpty()) {
			final Sprint theSprint = new Sprint();
			theSprint.setSprintName(sprintName);
			theSprint.setTeamName(teamName);

			for (final SprintProvider provider : this.sprintProviders) {
				provider.augmentSprint(theSprint);
			}

			for (final UserStory userStory : theSprint.getUserStories()) {
				for (final UserStoryProvider provider : this.userStoryProviders) {
					provider.augmentUserStory(userStory);
				}

				for (final Participant participant : userStory.getParticipants()) {
					theSprint.addOrUpdateParticipant(participant);
				}
			}

			entityManager.persist(theSprint);

			return theSprint;
		}

		return sprints.get(0);
	}

	@Override
	@Transactional
	public List<Sprint> getPossibleSprints(String teamName) {
		final List<Sprint> sprints = new ArrayList<>();
		for (final SprintProvider provider : this.sprintProviders) {
			sprints.addAll(provider.findPossibleSprints(teamName));
		}
		return unmodifiableList(sprints);
	}

	@Override
	public Sprint getCurrentSprintForTeam(String teamName) {
		return null;
	}

}
