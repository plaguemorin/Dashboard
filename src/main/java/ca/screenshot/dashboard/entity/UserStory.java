package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;

/**
 */
@XmlRootElement
@XmlType(name = "UserStory")
public class UserStory extends AbstractValueObject {

	private UserStoryStatus storyStatus;
	private String remoteIdentifier;
	private String title;
	private String description;

	private List<UserStoryTask> taskList = new ArrayList<>();
	private List<Participant> participantList = new ArrayList<>();

	public String getRemoteIdentifier() {
		return remoteIdentifier;
	}

	public void setRemoteIdentifier(String remoteIdentifier) {
		this.remoteIdentifier = remoteIdentifier;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addParticipant(Participant participant) {
		this.participantList.add(participant);
	}

	public void addTask(UserStoryTask userStoryTask) {
		this.taskList.add(userStoryTask);
	}

	public Collection<Participant> getParticipants() {
		return unmodifiableCollection(this.participantList);
	}

	public Collection<UserStoryTask> getTasks() {
		return unmodifiableCollection(this.taskList);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}
