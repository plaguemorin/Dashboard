package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.*;

import static java.util.Collections.*;

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

	@ManyToMany
	private List<UserStory> userStoryList = new ArrayList<>();

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
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getWorkDays() {
		return workDays;
	}

	public void setWorkDays(Integer workDays) {
		this.workDays = workDays;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	@XmlID
	@XmlAttribute(name = "sprintId")
	public String getSprintKey() {
		return sprintIdentity.getSprintKey();
	}

	public void setSprintKey(String sprintKey) {
		this.sprintIdentity.setSprintKey(sprintKey);
	}

	public void setTeamName(String teamName) {
		this.sprintIdentity.setTeamName(teamName);
	}

	@XmlAttribute(name = "teamId")
	public String getTeamName() {
		return this.sprintIdentity.getTeamName();
	}

	public void addUserStory(final UserStory userStory) {
		userStoryList.add(userStory);
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@XmlElementWrapper(name = "stories")
	@XmlElement(name = "story")
	@XmlIDREF
	public Collection<UserStory> getUserStories() {
		return unmodifiableCollection(this.userStoryList);
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
	private List<ParticipantRole> getParticipants_() {
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

	public UserStory getUserStory(String userStoryKey) {
		for (final UserStory userStory : userStoryList) {
			if (userStoryKey.equals(userStory.getStoryKey())) {
				return userStory;
			}
		}

		return null;
	}

}
