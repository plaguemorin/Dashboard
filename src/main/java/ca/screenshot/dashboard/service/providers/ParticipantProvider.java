package ca.screenshot.dashboard.service.providers;

import ca.screenshot.dashboard.entity.Participant;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:02 PM
 */
public interface ParticipantProvider {
	Participant findParticipantByUser(final String user);
}
