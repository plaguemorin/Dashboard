package ca.screenshot.dashboard.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:14 PM
 */
public class Sprint {
	private String sprintName;
	private String teamName;
	private List<UserStory> userStoryList = new ArrayList<>();

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamName() {
		return teamName;
	}


	public void updateOrCreate(final UserStory userStory) {
	}
}
