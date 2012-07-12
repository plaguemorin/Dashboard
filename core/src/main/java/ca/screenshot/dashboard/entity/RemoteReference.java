package ca.screenshot.dashboard.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

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

	public RemoteReference() {

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void dirty() {
		this.dirty = true;
	}

	public void clean() {
		this.dirty = false;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}
}
