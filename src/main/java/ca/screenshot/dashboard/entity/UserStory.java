package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;
import static java.util.UUID.randomUUID;

/**
 */
@XmlRootElement
@XmlType(name = "UserStory")
@Entity
public class UserStory extends AbstractSourcedGeneratedObject {
	@Id
	private String guid = randomUUID().toString();

	@Enumerated(EnumType.STRING)
	private UserStoryStatus storyStatus;

	private String title;

	@Column(length = 9000)
	private String description;

	@OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "userStory")
	private List<UserStoryTask> taskList = new ArrayList<>();

	@ManyToMany
	private List<Participant> participantList = new ArrayList<>();

	private Long storyPoints;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = (description == null || description.length() <= 9000) ? description : description.substring(0, 9000);
	}

	public void addTask(UserStoryTask userStoryTask) {
		this.taskList.add(userStoryTask);
		userStoryTask.setUserStory(this);
	}

	public Long getTotalEstimatedTime() {
		long i = 0;

		for (final UserStoryTask task : this.taskList) {
			i += task.getSecondsEstimated();
		}

		return i;
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
		super.updateWith(userStory);

		this.description = userStory.description;
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

	public void setStoryPoints(Long storyPoints) {
		this.storyPoints = storyPoints;
	}

	public Long getStoryPoints() {
		return storyPoints;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

}
