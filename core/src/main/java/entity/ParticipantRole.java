package entity;

import javax.xml.bind.annotation.*;

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

	@ManyToOne(optional = false)
	private Sprint sprint;

	@XmlIDREF
	@XmlValue
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@XmlAttribute(name = "role")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@XmlTransient
	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}
}
