package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
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
public class Participant extends AbstractSourcedGeneratedObject {
	@Id
	private String user;

	private String email;

	private String displayName;

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
}
