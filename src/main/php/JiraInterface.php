<?php

require_once("JiraConnection.php");
require_once("dashboardObjects.php");

class JiraInterface implements DashboardAugmenter
{
	/**
	 * @var \JiraConnection
	 */
	private $jiraConnection;

	private $statusList;

	public function __construct(JiraConnection $connection)
	{
		$this->jiraConnection = $connection;
	}

	protected function fetchStatusList()
	{
		if ($this->statusList == null) {
			$statues = $this->jiraConnection->getStatuses();
			foreach ($statues as $statusItem) {
				$this->statusList[(int)$statusItem->id] = $statusItem->name;
			}
		}
	}

	function fillDashboard(Dashboard $dashboard)
	{
		$this->fetchStatusList();
		$this->fetchCurrentSprintForTeamName($dashboard->teamName, $dashboard->sprint);
		$this->generateDayGraph($dashboard);
		$this->fetchUserStoriesForSprint($dashboard);
		$this->updateSprintStatistics($dashboard);
	}

	public function generateDayGraph(Dashboard $dashboard)
	{
		$date = strtotime($dashboard->sprint->startDate);

		for ($i = 0; $i <= $dashboard->sprint->totalDays; $i++) {

			$date += 86400;
		}

	}

	public function fetchCurrentSprintForTeamName($teamName, Sprint $sprint)
	{
		$todayTime = time();
		$days = $sprint->totalDays;

		$versions = $this->jiraConnection->getVersions($teamName);
		foreach ($versions as $value) {
			if ($value->releaseDate && !$value->released) {
				$time = strtotime($value->releaseDate);
				$nodays = floor(($time - $todayTime) / (60 * 60 * 24));

				if ($nodays > 0) {
					$daysLeft = ($days - $nodays) * 86400;

					$sprint->name = $value->name;
					$sprint->daysLeft = $nodays;
					$sprint->endDate = date("Y-m-d", $time);
				}
			}
		}

		return null;
	}

	public function fetchUserStoriesForSprint(Dashboard $dashboard)
	{
		$userStories = $this->jiraConnection->getIssuesFromJqlSearch("type =\"User story\" and fixVersion =\"" . addslashes($dashboard->sprint->name) . "\"", 1000);

		foreach ($userStories as $us) {
			$cleanUserStory = $this->buildUserStory($dashboard, $us);
			$dashboard->userStories[] = $cleanUserStory;
		}
	}

	/**
	 * @param \Dashboard $dashboard
	 * @param $item
	 * @param $buildSubTasks boolean true if should fetch sub-tasks
	 * @return UserStory
	 */
	private function buildUserStory(Dashboard $dashboard, $item, $buildSubTasks = true)
	{
		$userStory = new UserStory();
		$userStory->key = $item->key;
		$userStory->assignee = (array) $this->getParticipantGuidForJiraUser($dashboard, (string)$item->assignee);
		$userStory->title = trim($item->summary);
		$userStory->created = strtotime($item->created); // $item->reporter
		$userStory->updated = strtotime($item->updated);
		$userStory->status = $this->statusList[(int)$item->status];
		$userStory->numSubTasks = 0;
		$userStory->activityLog = array();
		$userStory->storyPoints = 0;
		$userStory->estimatedSeconds = 0;
		$userStory->isDone = ((int)$item->status == 6);
		$userStory->guid = (string)$item->id;

		if ($buildSubTasks) {
			foreach ($this->getSubTasks($item->key) as $subTask) {
				$sub = $this->buildUserStory($dashboard, $subTask, false);

				$userStory->assignee = array_merge($userStory->assignee, $sub->assignee);
				$userStory->activityLog = array_merge($userStory->activityLog, $sub->activityLog);

				$userStory->numSubTasks += $sub->numSubTasks + 1;
				$userStory->estimatedSeconds += $sub->estimatedSeconds;
			}
		}

		// Make sure we only have unique entries
		$userStory->assignee = array_values(array_unique($userStory->assignee));

		// Fetch resolved date
		$this->extractResolutionDate($item, $userStory);

		// Get local time info
		$this->extractCustomFields($item, $userStory);

		// Build up timeline for this user story
		$ret = new ActivityLogItem();

		$ret->time = $userStory->created;
		$ret->description = $userStory->title . " was created";
		$ret->pointsDelta = $userStory->storyPoints;
		$ret->source = "Jira";
		$ret->guid = "jira" . $userStory->guid . "created";
		$ret->who = $this->getParticipantGuidForJiraUser($dashboard, $item->reporter);

		$userStory->activityLog[] = $ret;

		if ($userStory->created != $userStory->updated) {
			$userStory->activityLog[] = $this->buildUpdateActivityLog($userStory);
		}

		if ($userStory->resolvedDate) {
			$userStory->activityLog[] = $this->buildResolvedActivityLog($userStory);
		}

		return $userStory;
	}

	private function getParticipantGuidForJiraUser(Dashboard $dashboard, $jiraUserName)
	{
		$participantGuid = $dashboard->getParticipantGuidByUsername($jiraUserName);

		if ($participantGuid == null) {
			$jiraUser = $this->jiraConnection->getUser($jiraUserName);
			$participantGuid = $dashboard->createParticipant($jiraUser->email, $jiraUser->fullname, $jiraUser->name);
		}

		return $participantGuid;
	}

	private function extractResolutionDate($us, UserStory $userStory)
	{
		$resolvedDate = $this->jiraConnection->getResolutionDateById($us->id);
		if (!empty($resolvedDate)) {
			$userStory->resolvedDate = strtotime($resolvedDate);
		}
	}

	private function extractCustomFields($us, UserStory $userStory)
	{
		foreach ($us->customFieldValues as $customField) {
			if ($customField->customfieldId == "customfield_10162") {
				$userStory->estimatedSeconds += (int)$customField->values[0];
			}
			if ($customField->customfieldId == "customfield_10220") {
				$userStory->storyPoints = (float)$customField->values[0];
			}
		}
	}

	private function getSubTasks($parentKey)
	{
		$jql = "parent = \"" . addslashes($parentKey) . "\"";
		$ret = $this->jiraConnection->getIssuesFromJqlSearch($jql);

		return $ret;
	}

	/**
	 * @param \UserStory $userStory
	 * @return ActivityLogItem
	 */
	private function buildUpdateActivityLog(UserStory $userStory)
	{
		$ret = new ActivityLogItem();

		$ret->time = $userStory->updated;
		$ret->description = $userStory->title . " was updated";
		$ret->pointsDelta = 0;
		$ret->source = "Jira";
		$ret->guid = "jira" . $userStory->guid . "updated";

		return $ret;
	}

	/**
	 * @param \UserStory $userStory
	 * @return ActivityLogItem
	 */
	private function buildResolvedActivityLog(UserStory $userStory)
	{
		$ret = new ActivityLogItem();

		$ret->time = $userStory->resolvedDate;
		$ret->description = $userStory->title . " was completed";
		$ret->pointsDelta = -$userStory->storyPoints;
		$ret->source = "Jira";
		$ret->guid = "jira" . $userStory->guid . "completed";

		return $ret;
	}

	/**
	 * @param Dashboard $dashboard
	 * @return void
	 */
	private function updateSprintStatistics(Dashboard $dashboard)
	{
		$dashboard->sprint->numberUserStories = count($dashboard->userStories);
		$dashboard->sprint->pointsTotal = 0;
		$dashboard->sprint->pointsToDo = 0;
		$dashboard->sprint->pointsDone = 0;

		foreach ($dashboard->userStories as &$userStory) {
			$dashboard->sprint->pointsTotal += $userStory->storyPoints;
			$dashboard->sprint->numberSubTasks += $userStory->numSubTasks;

			if ($userStory->status == 'Open') {
				$dashboard->sprint->pointsToDo += $userStory->storyPoints;
			} else {
				$dashboard->sprint->pointsDone += $userStory->storyPoints;
			}

			$dashboard->activityLog = array_merge($dashboard->activityLog, $userStory->activityLog);
			unset($userStory->activityLog);
		}
	}
}
