package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

	@XmlAttribute
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

	public void addTask(UserStoryTask userStoryTask) {
		this.taskList.add(userStoryTask);
	}

	@XmlTransient
	public Collection<Participant> getParticipants() {
		return unmodifiableCollection(this.participantList);
	}

	@XmlTransient
	public Collection<UserStoryTask> getTasks() {
		return unmodifiableCollection(this.taskList);
	}

	private void addOrUpdateTask(final UserStoryTask storyTask) {
		if (this.taskList.contains(storyTask)) {
			final UserStoryTask oldStoryTask = this.taskList.get(this.taskList.indexOf(storyTask));
			oldStoryTask.updateWith(storyTask);
		} else {
			this.taskList.add(storyTask);
		}
	}

	public void addOrUpdateParticipant(final Participant participant) {
		if (this.participantList.contains(participant)) {
			final Participant oldParticipant = this.participantList.get(this.participantList.indexOf(participant));
			oldParticipant.updateWith(participant);
		} else {
			this.participantList.add(participant);
		}
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void updateWith(UserStory userStory) {
		this.description = userStory.description;
		this.remoteIdentifier = userStory.remoteIdentifier;
		this.title = userStory.title;
		this.storyStatus = userStory.storyStatus;

		for (final Participant participant : userStory.participantList) {
			this.addOrUpdateParticipant(participant);
		}

		final Iterator<Participant> thisParticipantListIterator = this.participantList.iterator();
		while (thisParticipantListIterator.hasNext()) {
			if (!userStory.participantList.contains(thisParticipantListIterator.next())) {
				thisParticipantListIterator.remove();
			}
		}

		for (final UserStoryTask storyTask : userStory.taskList) {
			this.addOrUpdateTask(storyTask);
		}

		final Iterator<UserStoryTask> thisUserStoryTaskIterator = this.taskList.iterator();
		while (thisUserStoryTaskIterator.hasNext()) {
			if (!userStory.taskList.contains(thisUserStoryTaskIterator.next())) {
				thisUserStoryTaskIterator.remove();
			}
		}
	}
}
