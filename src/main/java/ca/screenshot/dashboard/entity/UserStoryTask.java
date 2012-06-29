package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import static java.util.UUID.randomUUID;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:27 PM
 */
@XmlRootElement
@Entity
public class UserStoryTask extends AbstractValueObject {
	@Id
	private String guid;

	private String title;
	private Long secondsEstimated;
	private Long secondsRemaining;
	private Long secondsWorked;

	@ManyToOne(optional = false)
	private UserStory userStory;

	@PrePersist
	public void prePersist() {
		if (this.guid == null) {
			this.guid = randomUUID().toString();
		}
	}

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
	public String getGuid() {
		return this.guid;
	}

	public void setGuid(final String guid) {
		this.guid = guid;
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
	}
}
