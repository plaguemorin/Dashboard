package ca.screenshot.dashboard.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
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
@MappedSuperclass
public abstract class AbstractLoggedValueObject extends AbstractValueObject implements LoggedObject {
	@OneToMany
	private List<ActivityLog> logList = new ArrayList<>();

	@XmlElementWrapper(name = "log")
	@XmlElement(name = "item")
	public Collection<ActivityLog> getActivityLog() {
		return unmodifiableCollection(logList);
	}

	public void addLog(final ActivityLog activityLog) {
		this.logList.add(activityLog);
	}

	@Override
	public void updateLog(final LoggedObject valueObject) {
		for (final ActivityLog log : valueObject.getActivityLog()) {
			if (this.logList.contains(log)) {
				this.logList.get(this.logList.indexOf(log)).updateWith(log);
			} else {
				this.logList.add(log);
			}
		}
	}
}
