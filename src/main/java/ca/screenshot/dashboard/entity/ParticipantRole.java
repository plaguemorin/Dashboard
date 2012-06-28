package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static java.util.UUID.randomUUID;

/**
 * User: plaguemorin
 * Date: 28/06/12
 * Time: 10:10 AM
 */
@XmlRootElement
@Entity
public class ParticipantRole {
	@Id
	private String id = randomUUID().toString();

	@ManyToOne
	private Participant participant;

	@Enumerated(EnumType.STRING)
	private Role role;

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
