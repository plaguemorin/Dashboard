package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

import static java.util.UUID.randomUUID;

/**
 * @author plaguemorin
 *         Date: 24/05/12
 *         Time: 12:48 PM
 */
@XmlRootElement
@Entity
public class ActivityLog extends AbstractValueObject {
	@Id
	private String guid;

	private String who;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SNAP_DATE")
	private Date when;

	private String what;

	@PrePersist
	public void prePersist() {
		if (this.guid == null) {
			this.guid = randomUUID().toString();
		}
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public void updateWith(final ActivityLog activityLog) {
		super.updateWith(activityLog);

		this.what = activityLog.what;
		this.who = activityLog.who;
		this.when = activityLog.when;
	}

	public String getGuid() {
		return this.getGuid();
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
}
