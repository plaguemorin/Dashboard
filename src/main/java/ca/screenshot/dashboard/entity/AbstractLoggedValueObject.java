package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;

/**
 * @author plaguemorin
 *         Date: 25/05/12
 *         Time: 2:05 PM
 */
public abstract class AbstractLoggedValueObject extends AbstractValueObject {
	private List<ActivityLog> logList = new ArrayList<>();

	@XmlElementWrapper(name = "log")
	@XmlElement(name = "item")
	public Collection<ActivityLog> getActivityLog() {
		return unmodifiableCollection(logList);
	}

	public void addLog(final ActivityLog activityLog) {
		this.logList.add(activityLog);
	}
}
