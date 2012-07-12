package ca.screenshot.dashboard.service.rest.vo;

import ca.screenshot.dashboard.entity.Sprint;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * User: plaguemorin
 * Date: 29/06/12
 * Time: 12:44 AM
 */
@XmlRootElement(name = "listOfSprints")
public class ListOfSprints {
	private Collection<Sprint> sprints;

	@XmlIDREF
	@XmlElementWrapper(name = "sprints")
	public Collection<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(Collection<Sprint> sprints) {
		this.sprints = sprints;
	}
}
