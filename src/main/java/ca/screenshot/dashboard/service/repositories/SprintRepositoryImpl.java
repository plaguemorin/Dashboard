package ca.screenshot.dashboard.service.repositories;

import ca.screenshot.dashboard.entity.Sprint;
import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.service.providers.SprintProvider;
import ca.screenshot.dashboard.service.providers.UserStoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;

/**
 * @author PLMorin
 *         16/05/12 9:00 PM
 */
@Repository
public class SprintRepositoryImpl implements SprintRepository {
	private final static Logger LOGGER = LoggerFactory.getLogger(SprintRepositoryImpl.class);

	@Autowired
	private List<SprintProvider> sprintProviders;

	@Autowired
	private List<UserStoryProvider> userStoryProviders;

	private Map<String, Sprint> sprintMap = new LinkedHashMap<>();

	@Override
	public Sprint getSprintForTeam(String teamName) {
		if (sprintMap.containsKey(teamName)) {
			return sprintMap.get(teamName);
		}

		this.buildSprintForTeam(teamName);


		return this.getSprintForTeam(teamName);
	}

	@Override
	public List<Sprint> getPossibleSprints(String teamName) {
		final List<Sprint> sprints = new ArrayList<>();
		for (final SprintProvider provider : this.sprintProviders) {
			sprints.addAll(provider.findPossibleSprints(teamName));
		}
		return unmodifiableList(sprints);
	}

	private void buildSprintForTeam(final String teamName) {
		for (final SprintProvider provider : this.sprintProviders) {
			final Sprint latestSprint = provider.findLatestSprint(teamName);
			if (latestSprint != null) {
				LOGGER.info("Found sprint for team: " + latestSprint.getSprintName());
				this.sprintMap.put(teamName, latestSprint);
				this.updateUserStoriesForSprint(latestSprint);
				return;
			}
		}
	}

	private void updateUserStoriesForSprint(Sprint latestSprint) {
		for (final UserStoryProvider storyProvider : this.userStoryProviders) {
			final List<UserStory> userStoriesForSprint = storyProvider.getUserStoriesForSprint(latestSprint);

			for (final UserStory userStory : userStoriesForSprint) {
				latestSprint.updateWith(userStory);
			}
		}
	}
}
