package ca.screenshot.dashboard.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static java.util.Collections.unmodifiableCollection;

/**
 */
@MappedSuperclass
public abstract class AbstractValueObject implements ValueObject {
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createdDate = new Date();

	@OneToMany
	private Collection<RemoteReference> references;

	@PrePersist
	public void prePersist() {
		this.modifiedDate = new Date();
	}

	public void updateWith(final ValueObject valueObject) {
		this.modifiedDate = valueObject.getModifiedDate();
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public RemoteReference addRemoteReference(String remoteReference) {
		if (this.references == null) {
			this.references = new ArrayList<>();
		}

		final RemoteReference reference = new RemoteReference(remoteReference);
		this.references.add(reference);
		return reference;
	}

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
