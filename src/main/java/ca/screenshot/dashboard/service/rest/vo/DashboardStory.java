package ca.screenshot.dashboard.service.rest.vo;

import ca.screenshot.dashboard.entity.UserStory;
import ca.screenshot.dashboard.entity.UserStoryTask;
import ca.screenshot.dashboard.entity.UserStoryTaskWork;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: plaguemorin
 * Date: 27/07/12
 * Time: 11:07 AM
 */
@XmlRootElement
public class DashboardStory {
	private String title;
	private Map<String, DashboardParticipant> participants = new HashMap<> ();

	public static DashboardStory fromUserStory(UserStory userStory) {
		final DashboardStory me = new DashboardStory();

		me.title = userStory.getTitle();
		for (final UserStoryTask task : userStory.getTasks()) {
			for (final UserStoryTaskWork taskWork : task.getWorkList()) {
				final String user = taskWork.getParticipant().getUser();

				if (!me.participants.containsKey(user)) {
					me.participants.put(user, DashboardParticipant.fromWorkLog(taskWork));
				} else {
					me.participants.get(user).addWork(taskWork.getWorkSeconds());
				}
			}
		}

		return me;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	@XmlElementWrapper(name = "participants")
	@XmlElement(name = "participant")
	public Collection<DashboardParticipant> getParticipants() {
		return participants.values();
	}
}
