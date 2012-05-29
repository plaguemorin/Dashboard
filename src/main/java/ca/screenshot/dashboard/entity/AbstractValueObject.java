package ca.screenshot.dashboard.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;
import java.util.Date;

/**
 */
@MappedSuperclass
public abstract class AbstractValueObject implements ValueObject, Serializable {
	@Id
	private String guid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createdDate = new Date();

	@Override
	@XmlID
	@XmlAttribute(name = "guid", required = true)
	public String getGuid() {
		return this.guid;
	}

	@PrePersist
	public void prePersist() {
		this.modifiedDate = new Date();
	}

	@Override
	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractValueObject)) return false;

		final AbstractValueObject that = (AbstractValueObject) o;

		return guid.equals(that.guid);
	}

	@Override
	public int hashCode() {
		return guid.hashCode();
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
