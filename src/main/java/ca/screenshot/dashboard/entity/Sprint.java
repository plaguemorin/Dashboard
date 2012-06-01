package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

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
public class Sprint extends AbstractSourcedGeneratedLoggedObject {
	@OneToMany(cascade = {CascadeType.PERSIST})
	private List<UserStory> userStoryList = new ArrayList<>();

	@ManyToMany
	private List<Participant> participantList = new ArrayList<>();

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startDate;

	@EmbeddedId
	private SprintIdentity sprintIdentity = new SprintIdentity();


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
	public Collection<Participant> getParticipants() {
		return unmodifiableCollection(this.participantList);
	}

	public void addOrUpdateParticipant(Participant p) {
		if (participantList.contains(p)) {
			final Participant oldParticipant = participantList.get(participantList.indexOf(p));
			oldParticipant.updateWith(p);
		} else {
			participantList.add(p);
		}
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
}
