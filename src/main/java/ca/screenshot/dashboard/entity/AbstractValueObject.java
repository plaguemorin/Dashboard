package ca.screenshot.dashboard.entity;

import java.io.Serializable;

/**
 */
public abstract class AbstractValueObject implements ValueObject, Serializable
{
	private String guid;


	@Override
	public String getGuid()
	{
		return this.guid;
	}

	@Override
	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof AbstractValueObject)) return false;

		final AbstractValueObject that = (AbstractValueObject) o;

		return guid.equals(that.guid);
	}

	@Override
	public int hashCode()
	{
		return guid.hashCode();
	}
}
