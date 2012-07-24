package entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.*;

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
	private Long secondsWorked;

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
		return secondsWorked;
	}

	public void setSecondsWorked(Long secondsWorked) {
		this.secondsWorked = secondsWorked;
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
}
