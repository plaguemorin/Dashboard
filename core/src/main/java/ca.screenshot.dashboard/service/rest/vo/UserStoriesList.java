package dashboard.service.rest.vo;

import dashboard.entity.UserStory;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 12:40 AM
 */
@XmlRootElement(name = "listOfUserStories")
public class UserStoriesList {
	private Collection<UserStory> userStories;

	@XmlIDREF
	@XmlElementWrapper(name = "stories")
	public Collection<UserStory> getUserStories() {
		return userStories;
	}

	public void setUserStories(Collection<UserStory> userStories) {
		this.userStories = userStories;
	}
}
