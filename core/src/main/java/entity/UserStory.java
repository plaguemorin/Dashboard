package entity;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
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
	@Id
	private String storyKey;

	@Enumerated(EnumType.STRING)
	private UserStoryStatus storyStatus;

	private String title;

	@Column(length = 9000)
	private String description;

	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "userStory")
	private List<UserStoryTask> taskList = new ArrayList<>();

	@ManyToOne(optional = false)
	private Product product;

	@ManyToOne(optional = true)
	private Milestone milestone;

	private int storyPoints;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = (description == null || description.length() <= 9000) ? description : description.substring(0, 9000);
	}

	/**
	 * Creates an opaque reference to another system's userstory
	 *
	 * @param remoteReference URI representing the remote reference
	 * @return the new userstory
	 */
	public UserStoryTask addTask(String remoteReference) {
		final UserStoryTask task = new UserStoryTask();
		task.addRemoteReference(remoteReference);

		this.addTask(task);
		return task;
	}

	public UserStoryTask addTask(String title, long estimateSeconds) {
		final UserStoryTask task = new UserStoryTask();
		task.setTitle(title);
		task.setSecondsEstimated(estimateSeconds);
		this.addTask(task);
		return task;
	}

	public UserStoryTask addTask(String title, int estimatedSeconds, String remoteReference) {
		final UserStoryTask task = this.addTask(title, estimatedSeconds);

		task.addRemoteReference(remoteReference);

		return task;
	}

	public void addTask(UserStoryTask userStoryTask) {
		this.taskList.add(userStoryTask);
		userStoryTask.setUserStory(this);
		userStoryTask.setTaskId(String.valueOf(this.taskList.size()));
	}

	public Long getTotalEstimatedTime() {
		long i = 0;

		for (final UserStoryTask task : this.taskList)
			i += task.getSecondsEstimated();

		return i;
	}

	@XmlTransient
	public Collection<Participant> getParticipants() {
		final Collection<Participant> participants = new ArrayList<>();
		// TODO: Fix me
		return participants;
	}

	@XmlIDREF
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")
	public Collection<UserStoryTask> getTasks() {
		return unmodifiableCollection(this.taskList);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setStoryPoints(int storyPoints) {
		this.storyPoints = storyPoints;
	}

	public int getStoryPoints() {
		return storyPoints;
	}

	@XmlID
	@XmlAttribute(name = "storyId")
	public String getStoryKey() {
		return storyKey;
	}

	public void setStoryKey(String key) {
		this.storyKey = key;
	}

	@XmlIDREF
	@XmlAttribute(name = "productId")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@XmlIDREF
	@XmlAttribute(name = "milestoneId")
	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public UserStoryStatus getStoryStatus() {
		return storyStatus;
	}

	public void setStoryStatus(UserStoryStatus storyStatus) {
		this.storyStatus = storyStatus;
	}

	public UserStoryTask getTask(final String taskId) {
		for (final UserStoryTask task : this.taskList) {
			if (taskId.equals(task.getTaskId())) {
				return task;
			}
		}

		return null;
	}
}
