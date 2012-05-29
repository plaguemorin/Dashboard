package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:27 PM
 */
@XmlRootElement
@Entity
public class UserStoryTask extends AbstractSourcedGeneratedLoggedObject {

	private String title;
	private Long minutesEstimated;

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

	public Long getMinutesEstimated() {
		return minutesEstimated;
	}

	public void setMinutesEstimated(Long minutesEstimated) {
		this.minutesEstimated = minutesEstimated;
	}
}
