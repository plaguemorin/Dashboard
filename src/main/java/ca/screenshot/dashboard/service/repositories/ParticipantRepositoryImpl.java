package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.service.providers.ParticipantAugmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:06 PM
 */
@Repository
public class ParticipantRepositoryImpl implements ParticipantRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantRepositoryImpl.class);

	@Autowired
	private List<ParticipantAugmenter> participantAugmenterList;

	@PersistenceContext
	private EntityManager entityManager;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Participant findParticipantByUser(final String participant) {
		final Query query = entityManager.createNamedQuery("Participant.findByUser");
		query.setParameter("user", participant.toLowerCase());

		try {
			return (Participant) query.getSingleResult();
		} catch (final NoResultException ignored) {
			LOGGER.debug("User \"" + participant + "\" was not found in DB");
		}

		final Participant user = new Participant();
		user.setUser(participant.toLowerCase());

		for (final ParticipantAugmenter participantAugmenter : this.participantAugmenterList) {
			participantAugmenter.augmentParticipant(user);
		}

		entityManager.persist(user);

		return user;
	}
}
