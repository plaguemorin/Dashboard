package ca.screenshot.dashboard.scm;

import ca.screenshot.dashboard.entity.ActivityLog;
import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.service.providers.ActivityLogProvider;
import ca.screenshot.dashboard.service.providers.ParticipantAugmenter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author plaguemorin
 *         Date: 25/05/12
 *         Time: 1:40 PM
 */
@Service
public class BitBucket implements ParticipantAugmenter, ActivityLogProvider {
	@Override
	public List<ActivityLog> getLog() {
		return null;
	}

	@Override
	public void augmentParticipant(Participant participant) {
	}
}
