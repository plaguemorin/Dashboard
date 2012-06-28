package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

import static java.util.Collections.unmodifiableCollection;


/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:14 PM
 */
@XmlRootElement
@Entity
@NamedQueries({
					  @NamedQuery(name = "Sprint.specificSprint", query = "SELECT a FROM Sprint a WHERE a.sprintIdentity.sprintName = :sprintName AND a.sprintIdentity.teamName = :teamName"),
					  @NamedQuery(name = "Sprint.allSprintForTeam", query = "SELECT a FROM Sprint a WHERE a.sprintIdentity.teamName = :teamName"),
					  @NamedQuery(name = "Sprint.currentSprintForTeam", query = "SELECT a FROM Sprint a WHERE a.sprintIdentity.teamName = :teamName AND a.endDate < current_timestamp() ORDER BY a.endDate ASC")
})
public class Sprint extends AbstractLoggedValueObject {
	@EmbeddedId
	private SprintIdentity sprintIdentity = new SprintIdentity();

	@OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
	private List<UserStory> userStoryList = new ArrayList<>();

	@OneToMany
	private List<ParticipantRole> participantList = new ArrayList<>();

	@OneToMany
	private List<SprintDayParticipant> sprintDayParticipants = new ArrayList<>();

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startDate;

	private Integer workDays;

	@Column(length = 9000)
	private String goals;


	public String getSprintName() {
		return sprintIdentity.getSprintName();
	}

	public void setSprintName(String sprintName) {
		this.sprintIdentity.setSprintName(sprintName);
	}

	public void setTeamName(String teamName) {
		this.sprintIdentity.setTeamName(teamName);
	}

	public String getTeamName() {
		return this.sprintIdentity.getTeamName();
	}

	public void addOrUpdateUserStory(final UserStory userStory) {
		if (userStoryList.contains(userStory)) {
			final UserStory oldUserStory = userStoryList.get(userStoryList.indexOf(userStory));
			oldUserStory.updateWith(userStory);
		} else {
			userStoryList.add(userStory);
		}
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	@XmlTransient
	public Collection<UserStory> getUserStories() {
		return unmodifiableCollection(this.userStoryList);
	}

	@XmlTransient
	public Map<Participant, Role> getParticipants() {
		final Map<Participant, Role> participantRoleMap = new HashMap<>();

		for (final ParticipantRole participantRole : this.participantList) {
			participantRoleMap.put(participantRole.getParticipant(), participantRole.getRole());
		}

		return participantRoleMap;
	}

	public void addOrUpdateParticipant(final Participant participant) {
		// Check if we have this participant
	}

	public UserStory getUserStory(String userStoryGuid) {
		for (final UserStory userStory : userStoryList) {
			if (userStoryGuid.equals(userStory.getGuid())) {
				return userStory;
			}
		}

		return null;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
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
}
