package ca.screenshot.dashboard.entity;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.*;
import java.util.*;

import static java.util.Collections.unmodifiableCollection;
import static javax.persistence.CascadeType.ALL;

/**
 * @author plaguemorin
 *         Date: 16/05/12
 *         Time: 7:27 PM
 */
@XmlRootElement
@Entity
public class UserStoryTask extends AbstractValueObject {
	@EmbeddedId
	private UserStoryTaskIdentifier taskId = new UserStoryTaskIdentifier();

	private String title;
	private Long secondsEstimated;
	private Long secondsRemaining;

	private Date doneDate;
	private Date startDate;
	private Date verifyDate;

	@OneToMany(cascade = ALL, mappedBy = "userStoryTask")
	private Collection<UserStoryTaskWork> workList = new ArrayList<>();

	@ManyToOne(optional = false)
	private UserStory userStory;

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public Long getSecondsEstimated() {
		return this.secondsEstimated;
	}

	public void setSecondsEstimated(final Long minutesEstimated) {
		this.secondsEstimated = minutesEstimated;
	}

	@XmlID
	@XmlAttribute(name = "userStoryTaskId")
	public String getKey() {
		return this.taskId.toString();
	}

	public void setTaskId(final String key) {
		this.taskId.setTaskId(key);
	}

	@XmlAttribute(name = "taskId")
	public String getTaskId() {
		return this.taskId.getTaskId();
	}

	public Long getSecondsRemaining() {
		return this.secondsRemaining;
	}

	public void setSecondsRemaining(final Long secondsRemaining) {
		this.secondsRemaining = secondsRemaining;
	}

	public Long getSecondsWorked() {
		long l = 0L;

		for (final UserStoryTaskWork taskWork : this.workList) {
			l += taskWork.getWorkSeconds();
		}

		return l;
	}

	@XmlIDREF
	@XmlAttribute(name = "storyId")
	public UserStory getUserStory() {
		return this.userStory;
	}

	public void setUserStory(final UserStory userStory) {
		this.userStory = userStory;
		this.taskId.setUserStory(this.userStory.getStoryKey());
	}

	@XmlIDREF
	@XmlElementWrapper(name = "work")
	@XmlElement(name = "log")
	public Collection<UserStoryTaskWork> getWorkList() {
		return unmodifiableCollection(this.workList);
	}

	public void addWorkLog(final Participant participant, final Long l) {
		final UserStoryTaskWork taskWork = new UserStoryTaskWork();

		taskWork.setParticipant(participant);
		taskWork.setUserStoryTask(this);
		taskWork.setWorkSeconds(l);

		this.workList.add(taskWork);
	}

	public UserStoryStatus getStatus() {
		if (this.doneDate != null)
			return UserStoryStatus.DONE;

		if (this.verifyDate != null)
			return UserStoryStatus.VERIFY;

		if (this.startDate != null)
			return UserStoryStatus.IN_PROGRESS;

		return UserStoryStatus.TODO;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public void setDoneDate(final Date doneDate) {
		this.doneDate = doneDate;
	}

	public Date getDoneDate() {
		return this.doneDate;
	}

	public void setVerifyDate(final Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public Date getVerifyDate() {
		return this.verifyDate;
	}

	public Collection<Participant> getParticipants() {
		final Collection<Participant> participants = new HashSet<>();

		for (final UserStoryTaskWork work : this.workList) {
			participants.add(work.getParticipant());
		}

		return participants;
	}
}
