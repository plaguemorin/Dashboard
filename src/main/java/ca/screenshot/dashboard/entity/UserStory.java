package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;
import static java.util.UUID.randomUUID;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;

/**
 */
@XmlRootElement
@XmlType(name = "UserStory")
@Entity
public class UserStory extends AbstractValueObject {
	@Id
	private String guid = null;

	@Enumerated(STRING)
	private UserStoryStatus storyStatus;

	private String title;

	@Column(length = 9000)
	private String description;

	@OneToMany(cascade = {PERSIST}, mappedBy = "userStory")
	private List<UserStoryTask> taskList = new ArrayList<>();

	@ManyToOne(optional = false)
	private Sprint sprint;

	private int storyPoints;

	@PrePersist
	public void prePersist() {
		this.guid = randomUUID().toString();
	}

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
		final Collection<Participant> participants = new ArrayList<>();
		// TODO: Fix me
		return participants;
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

	public void setStoryPoints(int storyPoints) {
		this.storyPoints = storyPoints;
	}

	public int getStoryPoints() {
		return storyPoints;
	}

	@XmlID
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@XmlIDREF
	@XmlAttribute(name = "sprintId")
	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprints) {
		this.sprint = sprints;
	}
}
