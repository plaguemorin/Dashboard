package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.RemoteReference;
import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.service.provider.SprintProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PLMorin
 *         16/05/12 9:00 PM
 */
@Repository
public class SprintAPIImpl implements SprintAPI {
	private final static Logger LOGGER = LoggerFactory.getLogger(SprintAPIImpl.class);

	@Autowired
	private List<SprintProvider> sprintProviders;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Sprint getSprintByKey(final String teamName, final String sprintKey) {
		LOGGER.debug("Getting sprint keyed \"" + sprintKey + "\"");

		final TypedQuery<Sprint> query = entityManager.createQuery("SELECT a from Sprint a WHERE a.sprintIdentity.sprintKey = :sprintKey AND a.sprintIdentity.teamName = :teamName", Sprint.class);
		query.setParameter("sprintKey", sprintKey);
		query.setParameter("teamName", teamName);

		return query.getSingleResult();
	}

	@Override
	@Transactional
	public List<Sprint> getSprintsForTeam(final String teamName) {
		final TypedQuery<Sprint> sprints = entityManager.createQuery("SELECT a FROM Sprint a WHERE a.sprintIdentity.teamName = :teamName", Sprint.class);
		sprints.setParameter("teamName", teamName);
		return sprints.getResultList();
	}

	@Override
	@Transactional
	public Sprint getCurrentSprintForTeam(final String teamName) {
		final List<Sprint> sprintsForTeam = this.getSprintsForTeam(teamName);

		return sprintsForTeam.get(sprintsForTeam.size() - 1);
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
		this.entityManager.merge(sprint);
	}

	@Override
	@Transactional
	public void updateRemote(Sprint sprint) {
		final Map<String, SprintProvider> sprintProviderMap;
		sprintProviderMap = new HashMap<>();

		for (final SprintProvider provider : this.sprintProviders) {
			sprintProviderMap.put(provider.getSystemId(), provider);
		}

		for (final RemoteReference reference : sprint.getRemoteReferences()) {
			if (reference.isDirty()) {
				sprintProviderMap.get(reference.getSystemId()).refreshSprint(sprint);
			}
		}
	}

}
