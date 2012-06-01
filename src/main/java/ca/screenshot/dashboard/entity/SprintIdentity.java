package ca.screenshot.dashboard.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SprintIdentity implements Serializable {
	private String sprintName;

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	private String teamName;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public SprintIdentity() {
	}
}