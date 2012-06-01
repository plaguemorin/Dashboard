package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlRootElement;

import static java.util.UUID.randomUUID;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:27 PM
 */
@XmlRootElement
@Entity
public class UserStoryTask extends AbstractSourcedGeneratedLoggedObject {
	@Id
	private String guid;

	private String title;
	private Long secondsEstimated;

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

	public void updateWith(final UserStoryTask storyTask) {
		super.updateWith(storyTask);
		super.updateGenerators(storyTask);

		this.title = storyTask.title;
	}

	public Long getSecondsEstimated() {
		return secondsEstimated;
	}

	public void setSecondsEstimated(Long minutesEstimated) {
		this.secondsEstimated = minutesEstimated;
	}

	public UserStory getUserStory() {
		return userStory;
	}

	public void setUserStory(UserStory userStory) {
		this.userStory = userStory;
	}
}
