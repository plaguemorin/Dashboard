package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:06 PM
 */
@Repository
public class ParticipantRepositoryImpl implements ParticipantRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Participant findParticipantByUser(final String participant) {
		final TypedQuery<Participant> query = entityManager.createNamedQuery("Participant.findByUser", Participant.class);
		query.setParameter("user", participant.toLowerCase());

		return query.getSingleResult();
	}
}
