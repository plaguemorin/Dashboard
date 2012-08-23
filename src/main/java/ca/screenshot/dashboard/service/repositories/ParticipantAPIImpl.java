package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:06 PM
 */
@Repository
public class ParticipantAPIImpl implements ParticipantAPI {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantAPIImpl.class);

	@PersistenceContext
	private EntityManager entityManager;


	@Override
	@Transactional
	public Participant findParticipantByUser(final String participant) {
		final TypedQuery<Participant> query = entityManager.createNamedQuery("Participant.findByUser", Participant.class);
		LOGGER.debug("Querying for user {}", participant);

		query.setParameter("user", participant.toLowerCase());

		try {
		return query.getSingleResult();
		} catch (final NoResultException e) {
			return this.create(participant);
		}
	}

	@Override
	@Transactional
	public Participant create(String userName) {
		final Participant participant = new Participant();
		participant.setUser(userName);

		this.entityManager.persist(participant);

		return participant;
	}

	@Override
	@Transactional
	public Participant create(String userName, String displayName, int hoursPerDay) {
		final Participant newParticipant = this.create(userName);

		newParticipant.setDisplayName(displayName);
		newParticipant.setHoursPerDay(hoursPerDay);
		this.save(newParticipant);

		return newParticipant;
	}

	@Override
	@Transactional
	public void save(Participant participant) {
		this.entityManager.merge(participant);
	}
}
