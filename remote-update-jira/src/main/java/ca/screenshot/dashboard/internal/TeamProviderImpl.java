package ca.screenshot.dashboard.internal;

import ca.screenshot.dashboard.service.providers.TeamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author PLMorin
 *         17/05/12 8:08 AM
 */
@Service
public class TeamProviderImpl implements TeamProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(TeamProviderImpl.class);

	@Scheduled(fixedRate = 60)
	public void checkUpdate() {
		LOGGER.debug("Checking for updates");
	}
}
