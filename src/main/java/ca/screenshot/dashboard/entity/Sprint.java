package ca.screenshot.dashboard.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;


/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 6:14 PM
 */
@XmlRootElement
public class Sprint extends AbstractValueObject
{
	private List<UserStory> userStoryList = new ArrayList<>();
	private String sprintName;
	private String teamName;
	private Calendar endDate;

	public String getSprintName()
	{
		return sprintName;
	}

	public void setSprintName(String sprintName)
	{
		this.sprintName = sprintName;
	}

	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}

	public String getTeamName()
	{
		return teamName;
	}

	public void updateOrCreate(final UserStory userStory)
	{
		if (userStoryList.contains(userStory)) {
			final UserStory oldUserStory = userStoryList.get(userStoryList.indexOf(userStory));
			oldUserStory.updateWith(userStory);
		} else {
			userStoryList.add(userStory);
		}
	}

	public void setEndDate(Calendar endDate)
	{
		this.endDate = endDate;
	}

	public Calendar getEndDate()
	{
		return endDate;
	}

	@XmlElementWrapper(name = "userstories")
	@XmlElement(name = "userstory")
	public Collection<UserStory> getUserStories()
	{
		return unmodifiableCollection(this.userStoryList);
	}
}
