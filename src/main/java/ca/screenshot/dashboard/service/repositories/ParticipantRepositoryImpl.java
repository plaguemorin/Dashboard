package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.service.providers.ParticipantProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
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
	private List<ParticipantProvider> participantProviderList;

	private List<Participant> participantCache = new LinkedList<>();

	@Override
	public Participant findParticipantByUser(String participant) {
		for (final Participant p : this.participantCache) {
			if (participant.equals(p.getUser())) {
				return p;
			}
		}

		LOGGER.debug("User \"" + participant + "\" was not found in cache");

		for (final ParticipantProvider participantProvider : this.participantProviderList) {
			final Participant user = participantProvider.findParticipantByUser(participant);
			if (user != null) {
				this.participantCache.add(user);

				LOGGER.info("User \"" + participant + "\" was added in cache by provider: " + participantProvider.getClass().getName());
				return user;
			}
		}

		LOGGER.warn("No provider was able to find a participant with user \"" + participant + "\"");

		// TODO: Refactor into a dummy provider
		final Participant p = new Participant();
		p.setUser(participant);
		this.participantCache.add(p);

		return p;
	}
}
