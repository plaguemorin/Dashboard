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
 * Work done on a task
 * <p/>
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

	@Override
	@PrePersist
	public void prePersist() {
		super.prePersist();

		if (this.workLogId == null) {
			this.workLogId = randomUUID().toString();
		}
	}

	@XmlIDREF
	@XmlAttribute(name = "taskId")
	public UserStoryTask getUserStoryTask() {
		return this.userStoryTask;
	}

	public void setUserStoryTask(final UserStoryTask userStoryTask) {
		this.userStoryTask = userStoryTask;
	}

	@XmlIDREF
	@XmlAttribute(name = "participantId")
	public Participant getParticipant() {
		return this.participant;
	}

	public void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@XmlID
	@XmlAttribute(name = "workLogId")
	public String getWorkLogId() {
		return this.workLogId;
	}

	public void setWorkLogId(final String workLogId) {
		this.workLogId = workLogId;
	}

	public Date getLogDate() {
		return (Date) this.logDate.clone();
	}

	public void setLogDate(final Date when) {
		this.logDate = when != null ? (Date) when.clone() : null;
	}

	public Long getWorkSeconds() {
		return this.workSeconds;
	}

	public void setWorkSeconds(final Long workSeconds) {
		this.workSeconds = workSeconds;
	}
}
