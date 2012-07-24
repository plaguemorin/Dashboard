package dashboard.service.rest.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: plaguemorin
 * Date: 26/06/12
 * Time: 5:15 PM
 */
@XmlRootElement
public class ListOfTeams {
	private List<String> teams;

	@XmlElement(name = "team")
	public List<String> getTeams() {
		return teams;
	}

	public void setTeams(List<String> teams) {
		this.teams = teams;
	}
}
