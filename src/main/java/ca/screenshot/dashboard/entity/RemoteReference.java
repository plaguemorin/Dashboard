package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.net.URI;
import java.util.Date;

import static java.util.UUID.randomUUID;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * User: plaguemorin
 * Date: 11/07/12
 * Time: 10:09 AM
 */
@Entity
@XmlRootElement
public class RemoteReference {
	@Id
	private String id;
	private boolean dirty;

	private String systemId;
	private String remoteId;

	@Temporal(TIMESTAMP)
	private Date lastUpdateTime;

	public RemoteReference() {

	}

	@PrePersist
	public void prePersit() {
		this.id = randomUUID().toString();
	}

	public RemoteReference(final String remoteReference) {
		try {
			final URI uri = URI.create(remoteReference);

			this.systemId = uri.getScheme();
			this.remoteId = uri.getSchemeSpecificPart();

		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException("remote reference was expected to be a URI", e);
		}
	}

	@XmlID
	@XmlAttribute(name = "refId")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(name = "needsRefresh")
	public boolean isDirty() {
		return dirty;
	}

	public void dirty() {
		this.dirty = true;
	}

	public void clean() {
		this.dirty = false;
	}

	@XmlAttribute(name = "systemId")
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@XmlValue
	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	@XmlAttribute(name = "updatedTime")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
