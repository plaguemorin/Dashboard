package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.*;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:14 PM
 */
@XmlRootElement
@Entity
public class Sprint extends AbstractValueObject {
	@EmbeddedId
	private final SprintIdentity sprintIdentity = new SprintIdentity();

	@OneToMany(mappedBy = "sprint")
	private List<UserStoryTask> userStoryTaskList = new ArrayList<>();

	@OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
	private List<ParticipantRole> participantList = new ArrayList<>();

	@OneToMany
	private List<SprintDayParticipant> sprintDayParticipants = new ArrayList<>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	private Integer workDays;

	@Column(length = 9000)
	private String goals;

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public Integer getWorkDays() {
		return this.workDays;
	}

	public void setWorkDays(final Integer workDays) {
		this.workDays = workDays;
	}

	public String getGoals() {
		return this.goals;
	}

	public void setGoals(final String goals) {
		this.goals = goals;
	}

	@XmlID
	@XmlAttribute(name = "sprintId")
	public String getSprintKey() {
		return this.sprintIdentity.getSprintKey();
	}

	public void setSprintKey(final String sprintKey) {
		this.sprintIdentity.setSprintKey(sprintKey);
	}

	public void setTeamName(final String teamName) {
		this.sprintIdentity.setTeamName(teamName);
	}

	@XmlAttribute(name = "teamId")
	public String getTeamName() {
		return this.sprintIdentity.getTeamName();
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	@XmlElementWrapper(name = "stories")
	@XmlElement(name = "story")
	@XmlIDREF
	public Collection<UserStory> getUserStories() {
		final List<UserStory> list = new ArrayList<>();
		for (final UserStoryTask task : this.userStoryTaskList) {
			if (!list.contains(task.getUserStory())) {
				list.add(task.getUserStory());
			}
		}

		return unmodifiableList(list);
	}

	@XmlTransient
	public Map<Participant, Role> getParticipants() {
		final Map<Participant, Role> participantRoleMap = new HashMap<>();

		for (final ParticipantRole participantRole : this.participantList) {
			participantRoleMap.put(participantRole.getParticipant(), participantRole.getRole());
		}

		return unmodifiableMap(participantRoleMap);
	}

	@XmlElementWrapper(name = "participants")
	@XmlElement(name = "participant")
	private List<ParticipantRole> getParticipants_JAXB() {
		return this.participantList;
	}

	public List<Participant> getParticipants(final Role role) {
		final List<Participant> list = new ArrayList<>();

		for (final ParticipantRole participantRole : this.participantList) {
			if (participantRole.getRole() == role) {
				list.add(participantRole.getParticipant());
			}
		}

		return unmodifiableList(list);
	}

	public void addParticipant(final Participant participant, final Role role) {
		final ParticipantRole participantRole = new ParticipantRole();
		participantRole.setRole(role);
		participantRole.setParticipant(participant);
		participantRole.setSprint(this);

		this.participantList.add(participantRole);
	}

	@XmlIDREF
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")
	public List<UserStoryTask> getUserStoryTask() {
		return unmodifiableList(this.userStoryTaskList);
	}

	public void addUserStoryTask(final UserStoryTask task) {
		this.userStoryTaskList.add(task);
	}

	public void addAllUserStoryTasks(final Collection<? extends UserStoryTask> tasks) {
		this.userStoryTaskList.addAll(tasks);
	}
}
