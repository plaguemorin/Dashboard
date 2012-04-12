package ca.screenshot.dashboard;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActivityStreamItem extends AbstractValueObject {
	private Actor actor;
	private Generator generator;

	private String title;
	private String content;

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
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
