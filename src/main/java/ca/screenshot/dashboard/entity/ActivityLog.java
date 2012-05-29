package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Date;

/**
 * @author plaguemorin
 *         Date: 24/05/12
 *         Time: 12:48 PM
 */
@XmlRootElement
@Entity
public class ActivityLog extends AbstractValueObject {
	@OneToOne
	private Participant who;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SNAP_DATE")
	private Date when;

	private String what;

	@XmlIDREF
	public Participant getWho() {
		return who;
	}

	public void setWho(Participant who) {
		this.who = who;
	}

	@XmlAttribute
	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

	@XmlValue
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
}
