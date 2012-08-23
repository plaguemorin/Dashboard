package ca.screenshot.dashboard.service.rest.vo;

import ca.screenshot.dashboard.entity.Participant;
import ca.screenshot.dashboard.entity.UserStoryTaskWork;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * User: plaguemorin
 * Date: 31/07/12
 * Time: 8:33 PM
 */
@XmlRootElement
public class DashboardParticipant {
	private String user;
	private long work = 0;

	public static DashboardParticipant fromParticipant(Participant participant) {
		final DashboardParticipant dashboardParticipant = new DashboardParticipant();

		dashboardParticipant.setUser(participant.getUser());

		return dashboardParticipant;
	}

	public static DashboardParticipant fromWorkLog(UserStoryTaskWork taskWork) {
		final DashboardParticipant dashboardParticipant = fromParticipant(taskWork.getParticipant());

		dashboardParticipant.addWork(taskWork.getWorkSeconds());

		return dashboardParticipant;
	}

	public void addWork(Long workSeconds) {
		this.work += workSeconds;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@XmlValue
	public String getUser() {
		return user;
	}

	@XmlAttribute(name = "workSeconds")
	public long getWork() {
		return work;
	}
}
