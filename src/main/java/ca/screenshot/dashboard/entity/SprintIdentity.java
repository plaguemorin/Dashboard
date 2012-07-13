package ca.screenshot.dashboard.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SprintIdentity implements Serializable {
	private String teamName;
	private String sprintKey;

	public String getSprintKey() {
		return sprintKey;
	}

	public void setSprintKey(String sprintKey) {
		this.sprintKey = sprintKey;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public SprintIdentity() {
	}
}