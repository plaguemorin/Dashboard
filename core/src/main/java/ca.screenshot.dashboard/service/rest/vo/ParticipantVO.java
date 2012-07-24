package dashboard.service.rest.vo;

import dashboard.entity.Participant;
import dashboard.entity.Role;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: plaguemorin
 * Date: 28/06/12
 * Time: 10:47 AM
 */
@XmlRootElement(name = "participantRole")
public class ParticipantVO {
	private Role role;
	private Participant participant;

	@XmlAttribute
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@XmlElement
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
}
