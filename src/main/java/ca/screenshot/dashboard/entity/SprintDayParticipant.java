package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import static java.util.UUID.randomUUID;

/**
 * User: plaguemorin
 * Date: 27/06/12
 * Time: 4:02 PM
 */
@Entity
@XmlRootElement
public class SprintDayParticipant {
	@Id
	private String id = randomUUID().toString();

	@ManyToOne
	private Participant participant;

	@ManyToOne
	private Sprint sprint;

	private Long dayNumber;

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public Long getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(Long dayNumber) {
		this.dayNumber = dayNumber;
	}
}
