package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:27 PM
 */
@XmlRootElement
public class UserStoryTask extends AbstractValueObject
{
	private String title;
	private String remoteIdentifier;

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setRemoteIdentifier(String remoteIdentifier)
	{
		this.remoteIdentifier = remoteIdentifier;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getRemoteIdentifier()
	{
		return this.remoteIdentifier;
	}

	public void updateWith(final UserStoryTask storyTask)
	{
		this.remoteIdentifier = storyTask.remoteIdentifier;
		this.title = storyTask.title;
	}

}
