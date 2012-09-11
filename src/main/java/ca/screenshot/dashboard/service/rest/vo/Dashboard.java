package ca.screenshot.dashboard.service.rest.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
	private List<DashboardStory> userStories = new ArrayList<>();
	private int points = 0;
	private String goals;
	private List<DashboardTask> endingSoonTasks = new ArrayList<>();
	private List<DashboardStory> verifyUserStories = new ArrayList<>();
	private List<DashboardStory> readyToDemoStory = new ArrayList<>();

	public void setTeamName(final String teamName) {
		this.teamName = teamName;
	}

	public void setSprintName(final String sprintName) {
		this.sprintName = sprintName;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public String getSprintName() {
		return this.sprintName;
	}

	public void setSprintLength(final int days) {
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
		return unmodifiableList(this.dayList);
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(final int points) {
		this.points = points;
	}

	public void setGoals(final String goals) {
		this.goals = goals;
	}

	public String getGoals() {
		return this.goals;
	}

	public void addUserStory(final DashboardStory userStory) {
		this.userStories.add(userStory);
	}

	@XmlElementWrapper(name = "userStories")
	@XmlElement(name = "userStory")
	private List<DashboardStory> getUserStoryList_JAXB() {
		return this.userStories;
	}

	public void addEndingSoonTask(final DashboardTask task) {
		this.endingSoonTasks.add(task);
	}

	@XmlElementWrapper(name = "endingSoonTasks")
	@XmlElement(name = "tasks")
	private List<DashboardTask> getEndingSoonTasks_JAXB() {
		return this.endingSoonTasks;
	}

	public void addUserStoryToVerify(final DashboardStory userStory) {
		this.verifyUserStories.add(userStory);
	}

	@XmlElementWrapper(name = "verifyUserStories")
	@XmlElement(name = "userStory")
	private List<DashboardStory> getToVerifyUserStories_JAXB() {
		return this.verifyUserStories;
	}

	public void addReadyToDemoStory(final DashboardStory dashboardStory) {
		this.readyToDemoStory.add(dashboardStory);
	}

	@XmlElementWrapper(name = "readyToDemoUserStories")
	@XmlElement(name = "userStory")
	private List<DashboardStory> getReadyToDemoUserStories_JAXB() {
		return this.readyToDemoStory;
	}

}
