package dashboard.service.repositories;

import dashboard.entity.Participant;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:03 PM
 */
public interface ParticipantAPI {
	Participant findParticipantByUser(final String participant);

	Participant create(String userName);

	Participant create(String userName, String displayName, int hoursPerDay);

	void save(Participant participant);
}
