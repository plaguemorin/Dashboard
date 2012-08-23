package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;

/**
 */
@XmlRootElement(name = "story")
@XmlType(name = "story")
@Entity
@NamedQueries({
					  @NamedQuery(name = "UserStories.All", query = "SELECT a FROM UserStory a"),
					  @NamedQuery(name = "UserStories.ByKey", query = "SELECT a FROM UserStory a WHERE a.storyKey = :key")
})
public class UserStory extends AbstractValueObject {
	private static final int DESCRIPTION_MAX_LENGTH = 9000;
	

	@Id
	private String storyKey;

	private String title;

	@Column(length = DESCRIPTION_MAX_LENGTH)
	private String description;

	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "userStory")
	private List<UserStoryTask> taskList = new ArrayList<>();

	@ManyToOne(optional = false)
	private Product product;

	@ManyToOne(optional = true)
	private Milestone milestone;

	private int storyPoints;

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setDescription(final String description) {
		this.description = description == null || description.length() <= DESCRIPTION_MAX_LENGTH ? description : description.substring(0, DESCRIPTION_MAX_LENGTH);
	}

	/**
	 * Creates an opaque reference to another system's userstory
	 *
	 * @param remoteReference URI representing the remote reference
	 * @return the new userstory
	 */
	public UserStoryTask addTask(final String remoteReference) {
		final UserStoryTask task = new UserStoryTask();
		task.addRemoteReference(remoteReference);
		task.requestRemoteRefresh();
		this.addTask(task);
		return task;
	}

	public UserStoryTask addTask(final String title, final long estimateSeconds) {
		final UserStoryTask task = new UserStoryTask();
		task.setTitle(title);
		task.setSecondsEstimated(estimateSeconds);
		this.addTask(task);
		return task;
	}

	public UserStoryTask addTask(final String title, final long estimatedSeconds, final String remoteReference) {
		final UserStoryTask task = this.addTask(title, estimatedSeconds);

		task.addRemoteReference(remoteReference);

		return task;
	}

	public void addTask(final UserStoryTask userStoryTask) {
		this.taskList.add(userStoryTask);
		userStoryTask.setUserStory(this);
		userStoryTask.setTaskId(String.valueOf(this.taskList.size()));
	}

	public Long getTotalEstimatedTime() {
		long l = 0L;

		for (final UserStoryTask task : this.taskList) {
			l += task.getSecondsEstimated();
		}

		return l;
	}

	@XmlTransient
	public Collection<Participant> getParticipants() {
		final Collection<Participant> participants = new HashSet<>();

		for (final UserStoryTask task : this.taskList) {
			participants.addAll(task.getParticipants());
		}

		return participants;
	}

	@XmlIDREF
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")
	public Collection<UserStoryTask> getTasks() {
		return unmodifiableCollection(this.taskList);
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setStoryPoints(final int storyPoints) {
		this.storyPoints = storyPoints;
	}

	public int getStoryPoints() {
		return this.storyPoints;
	}

	@XmlID
	@XmlAttribute(name = "storyId")
	public String getStoryKey() {
		return this.storyKey;
	}

	public void setStoryKey(final String key) {
		this.storyKey = key;
	}

	@XmlIDREF
	@XmlAttribute(name = "productId")
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(final Product product) {
		this.product = product;
	}

	@XmlIDREF
	@XmlAttribute(name = "milestoneId")
	public Milestone getMilestone() {
		return this.milestone;
	}

	public void setMilestone(final Milestone milestone) {
		this.milestone = milestone;
	}

	public UserStoryTask getTask(final String taskId) {
		for (final UserStoryTask task : this.taskList) {
			if (taskId.equals(task.getTaskId())) {
				return task;
			}
		}

		throw new IllegalArgumentException("taskId referees to an invalid task");
	}

	public UserStoryStatus getCurrentStatus() {
		int minWeight = UserStoryStatus.DONE.getWeight();

		for (final UserStoryTask task : this.taskList) {
			minWeight = Math.min(task.getStatus().getWeight(), minWeight);
		}

		return UserStoryStatus.fromWeight(minWeight);
	}

	@Override
	public String toString() {
		return "UserStory{" +
					   "storyKey='" + this.storyKey + '\'' +
					   ", title='" + this.title + '\'' +
					   '}';
	}
}
