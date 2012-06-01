package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 */
@MappedSuperclass
public abstract class AbstractValueObject implements ValueObject, Serializable {
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createdDate = new Date();

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
}
