package ca.screenshot.dashboard.service.providers.scm;

import ca.screenshot.dashboard.entity.ActivityLog;
import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.service.providers.ActivityLogProvider;
import ca.screenshot.dashboard.service.providers.ParticipantProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.hg.core.HgBadArgumentException;
import org.tmatesoft.hg.repo.HgLookup;
import org.tmatesoft.hg.repo.HgRemoteRepository;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.List;

/**
 * @author plaguemorin
 *         Date: 24/05/12
 *         Time: 11:35 AM
 */
@Service
public class MercurialProvider implements ParticipantProvider, ActivityLogProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(MercurialProvider.class);

	@Value("${mercurial.repositoryUrl}")
	private URL repositoryUrl;
	private HgRemoteRepository hgRemote;

	@PostConstruct
	public void init() throws HgBadArgumentException {
		LOGGER.info("Mercurial remote repository url is: \"" + repositoryUrl.toString() + "\"");

		hgRemote = new HgLookup().detect(repositoryUrl);
	}

	@Override
	public Participant findParticipantByUser(String user) {
		return null;
	}

	@Override
	public List<ActivityLog> getLog() {
		return null;
	}
}
