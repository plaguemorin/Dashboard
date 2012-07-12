package ca.screenshot.dashboard.scm;

import ca.screenshot.dashboard.entity.ActivityLog;
import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.service.providers.ActivityLogProvider;
import ca.screenshot.dashboard.service.providers.ParticipantAugmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.tmatesoft.hg.core.*;
import org.tmatesoft.hg.repo.HgBundle;
import org.tmatesoft.hg.repo.HgLookup;
import org.tmatesoft.hg.repo.HgRemoteRepository;
import org.tmatesoft.hg.repo.HgRepository;
import org.tmatesoft.hg.util.CancelledException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @author plaguemorin
 *         Date: 24/05/12
 *         Time: 11:35 AM
 */
//@Service
public class MercurialProvider implements ParticipantAugmenter, ActivityLogProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(MercurialProvider.class);

	@Value("${mercurial.repositoryUrl}")
	private URL repositoryUrl;

	@Value("${mercurial.repositoryCloneTo}")
	private File localRepositoryPath;

	private HgRemoteRepository hgRemote;
	private HgRepository repository;

	@PostConstruct
	public void init() throws HgBadArgumentException {
		LOGGER.info("Mercurial remote repository url is: \"" + repositoryUrl.toString() + "\"");

		hgRemote = new HgLookup().detect(repositoryUrl);

		try {
			hgRemote.getChanges(hgRemote.heads()).inspectAll(new HgBundle.Inspector() {
				@Override
				public void changelogStart() {
					LOGGER.info("change log start");
				}

				@Override
				public void changelogEnd() {
					LOGGER.info("change log end");
				}

				@Override
				public void manifestStart() {
					LOGGER.info("manifest start");
				}

				@Override
				public void manifestEnd() {
					LOGGER.info("manifest end");
				}

				@Override
				public void fileStart(String s) {
					LOGGER.info("file start " + s);
				}

				@Override
				public void fileEnd(String s) {
					LOGGER.info("file end " + s);
				}

				@Override
				public boolean element(HgBundle.GroupElement groupElement) {
					LOGGER.info(groupElement.toString());
					return true;
				}
			});
		} catch (HgRemoteConnectionException e) {
			LOGGER.error("Unable to connect to remote repository", e);
		}

		if (localRepositoryPath.exists() && initLocalRepository()) {
			LOGGER.info("Using local repository");
		} else {
			LOGGER.info("Cloning repository to " + this.localRepositoryPath.toString());
			if (cloneRemoteRepository()) {
				LOGGER.info("Cloned repository");
			}
		}
	}

	private boolean initLocalRepository() {
		try {
			this.repository = new HgLookup().detect(this.localRepositoryPath);
		} catch (HgRepositoryNotFoundException e) {
			LOGGER.warn("The repository at " + this.localRepositoryPath.toString() + " is not readable", e);
			return false;
		}

		return true;
	}

	private boolean cloneRemoteRepository() {
		final HgCloneCommand cloneCommand = new HgCloneCommand();
		cloneCommand.source(this.hgRemote);
		cloneCommand.destination(this.localRepositoryPath);

		if (!this.localRepositoryPath.exists()) {
			if (!this.localRepositoryPath.mkdir()) {
				LOGGER.error("Unable to create directory for cloning repository");
				return false;
			}
		}

		try {
			this.repository = cloneCommand.execute();
		} catch (HgException | CancelledException e) {
			LOGGER.error("Unable to clone remote repository", e);
			return false;
		}
		return true;
	}

	@Override
	public List<ActivityLog> getLog() {
		return null;
	}

	@Scheduled(fixedRate = 60)
	public void scanRepository() {
		LOGGER.info("Scanning for remote changes in " + hgRemote.toString());

		final HgIncomingCommand incomingCommand = new HgIncomingCommand(this.repository);
		incomingCommand.against(hgRemote);
		try {
			final List<Nodeid> nodeids = incomingCommand.executeLite();
			for (final Nodeid nodeid : nodeids) {
				LOGGER.info("Remote change: " + nodeid.toString());
			}
		} catch (HgException | CancelledException e) {
			LOGGER.error("Unable to fetch changes", e);
		}

	}

	@Override
	public void augmentParticipant(Participant participant) {
	}
}
