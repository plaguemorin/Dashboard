package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Date;

import static java.util.UUID.randomUUID;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * User: plaguemorin
 * Date: 13/07/12
 * Time: 5:58 PM
 */
@Entity
@XmlRootElement
public class UserStoryTaskWork extends AbstractValueObject {
	@Id
	private String workLogId;

	@ManyToOne(optional = false)
	private UserStoryTask userStoryTask;

	@ManyToOne
	private Participant participant;

	@Temporal(TIMESTAMP)
	private Date logDate = new Date();

	private Long workSeconds;

	@PrePersist
	public void prePersist() {
		if (this.workLogId == null) {
			this.workLogId = randomUUID().toString();
		}
	}

	@XmlIDREF
	@XmlAttribute(name = "taskId")
	public UserStoryTask getUserStoryTask() {
		return userStoryTask;
	}

	public void setUserStoryTask(UserStoryTask userStoryTask) {
		this.userStoryTask = userStoryTask;
	}

	@XmlIDREF
	@XmlAttribute(name = "participantId")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@XmlID
	@XmlAttribute(name = "workLogId")
	public String getWorkLogId() {
		return workLogId;
	}

	public void setWorkLogId(String workLogId) {
		this.workLogId = workLogId;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date when) {
		this.logDate = when;
	}

	public Long getWorkSeconds() {
		return workSeconds;
	}

	public void setWorkSeconds(Long workSeconds) {
		this.workSeconds = workSeconds;
	}
}
