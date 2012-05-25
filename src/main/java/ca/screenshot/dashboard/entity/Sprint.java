package ca.screenshot.dashboard.entity;

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
public class Sprint extends AbstractLoggedValueObject {
	private List<UserStory> userStoryList = new ArrayList<>();
	private List<Participant> participantList = new ArrayList<>();
	private String sprintName;
	private String teamName;
	private Calendar endDate;

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
}
