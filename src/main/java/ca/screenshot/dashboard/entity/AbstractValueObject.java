package ca.screenshot.dashboard.entity;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static java.util.Collections.unmodifiableCollection;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 */
@MappedSuperclass
public abstract class AbstractValueObject implements ValueObject {
	@Temporal(TIMESTAMP)
	private Date modifiedDate;

	@Temporal(TIMESTAMP)
	@Column(updatable = false)
	private Date createdDate = new Date();

	@OneToMany(cascade = ALL)
	private Collection<RemoteReference> references = new ArrayList<>();

	@PrePersist
	public void prePersist() {
		this.modifiedDate = new Date();
	}

	public void updateWith(final ValueObject valueObject) {
		this.modifiedDate = valueObject.getModifiedDate();
	}

	@Override
	@XmlAttribute(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	@XmlAttribute(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public RemoteReference getRemoteReferenceForSystemId(final String systemId) {
		for (final RemoteReference reference : this.references) {
			if (systemId.equals(reference.getSystemId())) {
				return reference;
			}
		}

		return null;
	}

	public RemoteReference addRemoteReference(String remoteReference) {
		final RemoteReference reference = new RemoteReference(remoteReference);
		this.references.add(reference);
		return reference;
	}

	public void addRemoteReference(String systemId, String remoteId) {
		this.addRemoteReference(systemId + ":"  +  remoteId);
	}

	@XmlElementWrapper(name = "remoteReferences")
	@XmlElement(name = "reference")
	@XmlIDREF
	public Collection<RemoteReference> getRemoteReferences() {
		return unmodifiableCollection(this.references);
	}

	public void requestRemoteRefresh() {
		for (final RemoteReference reference : this.references) {
			reference.dirty();
		}
	}

	public boolean needsRemoteRefresh() {
		for (final RemoteReference reference : this.references) {
			if (reference.isDirty()) {
				return true;
			}
		}
		return false;
	}

}
