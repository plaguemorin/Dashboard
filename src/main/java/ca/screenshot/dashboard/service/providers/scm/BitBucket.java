package ca.screenshot.dashboard.service.providers.scm;

import ca.screenshot.dashboard.entity.ActivityLog;
import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.service.providers.ActivityLogProvider;
import ca.screenshot.dashboard.service.providers.ParticipantProvider;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author plaguemorin
 *         Date: 25/05/12
 *         Time: 1:40 PM
 */
@Service
public class BitBucket implements ParticipantProvider, ActivityLogProvider {
	@Override
	public List<ActivityLog> getLog() {
		return null;
	}

	@Override
	public Participant findParticipantByUser(String user) {
		return null;
	}
}
