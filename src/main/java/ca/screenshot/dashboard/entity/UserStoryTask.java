package ca.screenshot.dashboard.entity;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;
import static javax.persistence.CascadeType.ALL;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:27 PM
 */
@XmlRootElement
@Entity
public class UserStoryTask extends AbstractValueObject {
	@EmbeddedId
	private UserStoryTaskIdentifier taskId = new UserStoryTaskIdentifier();

	private String title;
	private Long secondsEstimated;
	private Long secondsRemaining;

	@OneToMany(cascade = ALL, mappedBy = "userStoryTask")
	private Collection<UserStoryTaskWork> workList = new ArrayList<>();

	@ManyToOne(optional = false)
	private UserStory userStory;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public Long getSecondsEstimated() {
		return secondsEstimated;
	}

	public void setSecondsEstimated(Long minutesEstimated) {
		this.secondsEstimated = minutesEstimated;
	}

	@XmlID
	public String getKey() {
		return this.taskId.toString();
	}

	public void setTaskId(final String key) {
		this.taskId.setTaskId(key);
	}

	@XmlAttribute(name = "taskId")
	public String getTaskId() {
		return this.taskId.getTaskId();
	}

	public Long getSecondsRemaining() {
		return secondsRemaining;
	}

	public void setSecondsRemaining(Long secondsRemaining) {
		this.secondsRemaining = secondsRemaining;
	}

	public Long getSecondsWorked() {
		long i = 0;

		for (final UserStoryTaskWork taskWork : this.workList) {
			i += taskWork.getWorkSeconds();
		}

		return i;
	}

	@XmlIDREF
	@XmlAttribute(name = "storyId")
	public UserStory getUserStory() {
		return userStory;
	}

	public void setUserStory(UserStory userStory) {
		this.userStory = userStory;
		this.taskId.setUserStory(this.userStory.getStoryKey());
	}

	@XmlIDREF
	@XmlElementWrapper(name = "work")
	@XmlElement(name = "log")
	public Collection<UserStoryTaskWork> getWorkList() {
		return unmodifiableCollection(workList);
	}

	public void addWorkLog(final Participant participant, final Long i) {
		final UserStoryTaskWork taskWork = new UserStoryTaskWork();

		taskWork.setParticipant(participant);
		taskWork.setUserStoryTask(this);
		taskWork.setWorkSeconds(i);

		this.workList.add(taskWork);
	}
}
