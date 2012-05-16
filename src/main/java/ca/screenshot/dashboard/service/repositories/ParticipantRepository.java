package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Participant;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:03 PM
 */
public interface ParticipantRepository {
	Participant findParticipantByUser(final String participant);
}
