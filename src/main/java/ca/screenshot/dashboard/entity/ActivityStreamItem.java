package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActivityStreamItem extends AbstractValueObject {
	private Participant actor;
	private Generator generator;

	private String title;
	private String content;

	public Participant getActor() {
		return actor;
	}

	public void setParticipant(Participant actor) {
		this.actor = actor;
	}

	public Generator getGenerator() {
		return generator;
	}

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
