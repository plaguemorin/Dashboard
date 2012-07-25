package ca.screenshot.dashboard.service.rest.vo;

import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.unmodifiableList;

/**
 * @author plaguemorin
 *         Date: 17/05/12
 *         Time: 11:04 AM
 */
@XmlRootElement
public class Dashboard {
	private String teamName;
	private String sprintName;
	private List<SprintDay> dayList = EMPTY_LIST;
	private List<UserStory> userStories = new ArrayList<>();
	private int points = 0;
	private String goals;
	private List<UserStoryTask> endingSoonTasks = new ArrayList<>();

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public String getTeamName() {
		return teamName;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintLength(int days) {
		final List<SprintDay> newSprint = new ArrayList<>(days);

		// Oh nice
		if (this.dayList.size() > days) {

		}

		newSprint.addAll(this.dayList);
		this.dayList.clear();

		this.dayList = newSprint;
	}

	@XmlElementWrapper(name = "days")
	@XmlElement(name = "day")
	public List<SprintDay> getDayList() {
		return unmodifiableList(dayList);
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getGoals() {
		return goals;
	}

	public void addUserStory(UserStory userStory) {
		this.userStories.add(userStory);
	}

	@XmlElementWrapper(name = "userStories")
	@XmlElement(name = "userStory")
	@XmlIDREF
	private List<UserStory> getUserStoryList_JAXB() {
		return this.userStories;
	}

	public void addEndingSoonTask(UserStoryTask task) {
		this.endingSoonTasks.add(task);
	}

	@XmlElementWrapper(name = "endingSoonTasks")
	@XmlElement(name = "tasks")
	@XmlIDREF
	private List<UserStoryTask> getEndingSoonTasks_JAXB() {
		return this.endingSoonTasks;
	}
}
