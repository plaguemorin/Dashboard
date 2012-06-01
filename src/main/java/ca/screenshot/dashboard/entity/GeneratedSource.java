package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

import static java.util.UUID.randomUUID;

/**
 * @author plaguemorin
 *         Date: 29/05/12
 *         Time: 10:51 AM
 */
@Entity
@XmlRootElement
public class GeneratedSource extends AbstractValueObject {
	@Id
	private String guid;

	private String remoteIdentifier;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;

	@PrePersist
	public void prePersist() {
		if (this.guid == null) {
			this.guid = randomUUID().toString();
		}
	}

	public String getRemoteIdentifier() {
		return remoteIdentifier;
	}

	public void setRemoteIdentifier(String remoteIdentifier) {
		this.remoteIdentifier = remoteIdentifier;
	}

	public void updateWith(final GeneratedSource generatedSource) {
		this.remoteIdentifier = generatedSource.remoteIdentifier;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
