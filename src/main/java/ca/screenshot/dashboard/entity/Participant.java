package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:04 PM
 */
@XmlRootElement
@Entity
@NamedQueries({
					  @NamedQuery(name = "Participant.findByUser", query = "SELECT a FROM Participant a WHERE a.user = :user")
})
public class Participant extends AbstractValueObject {
	@Id
	private String user;

	@Column(unique = true)
	private String email;

	private String displayName;

	private Integer hoursPerDay;

	@XmlAttribute(name = "user")
	@XmlID
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void updateWith(Participant participant) {
		this.user = participant.user;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Participant)) return false;

		Participant that = (Participant) o;

		return user.equals(that.user);

	}

	@Override
	public int hashCode() {
		return user.hashCode();
	}

	public Integer getHoursPerDay() {
		return hoursPerDay;
	}

	public void setHoursPerDay(Integer hoursPerDay) {
		this.hoursPerDay = hoursPerDay;
	}
}
