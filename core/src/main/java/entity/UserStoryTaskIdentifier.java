package entity;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * User: plaguemorin
 * Date: 10/07/12
 * Time: 8:12 PM
 */
@XmlRootElement
@Embeddable
public class UserStoryTaskIdentifier implements Serializable {
	private String userStory;
	private String taskId;

	public String getUserStory() {
		return userStory;
	}

	public void setUserStory(String userStory) {
		this.userStory = userStory;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return userStory + ':' + taskId;
	}
}
