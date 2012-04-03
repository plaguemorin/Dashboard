<?php

class Sprint
{
	public $name;
	public $daysLeft;
	public $totalDays;
	public $startDate;
	public $endDate;

	public $numberSubTasks;
	public $numberUserStories;

	public $pointsToDo;
	public $pointsDone;
	public $pointsTotal;
}

class BuildStatus
{
	public $buildName;
	public $isOk;
	public $isBuilding;
}

class ServerStatus
{
	public $serverName;
	public $isOk;
}

class UserStory
{

	/**
	 * @var string
	 */
	public $guid;

	/**
	 * @var string
	 */
	public $key;

	/**
	 * @var string
	 */
	public $title;

	/**
	 * @var string
	 */
	public $updated;

	/**
	 * @var string
	 */
	public $created;

	/**
	 * @var int
	 */
	public $estimatedSeconds;

	/**
	 * @var int
	 */
	public $storyPoints;

	/**
	 * @var string
	 */
	public $assignee;

	/**
	 * @var string
	 */
	public $status;

	/**
	 * @var string
	 */
	public $resolvedDate;

	/**
	 * @var int
	 */
	public $numSubTasks;

	/**
	 * @var boolean
	 */
	public $isDone;

	/**
	 * @var ActivityLogItem[]
	 */
	public $activityLog;
}

class Participant
{
	/**
	 * @var string
	 */
	public $guid;

	/**
	 * @var string
	 */
	public $email;

	/**
	 * @var string
	 */
	public $displayName;

	/**
	 * @var string[]
	 */
	public $aliases;
}

class SprintDay
{
	/**
	 * @var int
	 */
	public $dayNum;

	/**
	 * @var int
	 */
	public $date;

	/**
	 * @var int
	 */
	public $numberToDo;

	/**
	 * @var int
	 */
	public $numberInProgress;

	/**
	 * @var int
	 */
	public $numberDone;
}

class ActivityLogItem
{
	/**
	 * @var string
	 */
	public $description;

	/**
	 * @var int
	 */
	public $time;

	/**
	 * @var int
	 */
	public $pointsDelta;

	/**
	 * @var string
	 */
	public $who;

	/**
	 * @var string
	 */
	public $source;

	/**
	 * @var int
	 */
	public $dayNum;

	/**
	 * @var string
	 */
	public $guid;
}

class Dashboard
{
	public $teamName;
	public $generatedTime;

	public $quote;

	/**
	 * @var SprintDay[]
	 */
	public $sprintDays;

	/**
	 * @var Sprint
	 */
	public $sprint;

	/**
	 * @var BuildStatus[]
	 */
	public $builds;

	/**
	 * @var ServerStatus[]
	 */
	public $serverStatus;

	/**
	 * @var ActivityLogItem[]
	 */
	public $activityLog;

	/**
	 * @var UserStory[]
	 */
	public $userStories;

	/**
	 * @var Participant[]
	 */
	public $participants;

	/**
	 * Gets the GUID of a participant by email, if it's not available then
	 * it will get created
	 *
	 * @param string $param1 email of the participant
	 * @return string
	 */
	public function getParticipantGuidByUsername($param1)
	{
		foreach (((array)$this->participants) as $participant) {
			if (in_array($param1, $participant->aliases)) {
				return $participant->guid;
			}
		}

		return null;
	}

	public function createParticipant($email, $displayName, $username)
	{
		$participant = new Participant();
		$participant->email = $email;
		$participant->guid = sha1($email);
		$participant->displayName = $displayName;
		$participant->aliases = array_unique(array($username, $email));

		if (!is_array($this->participants)) {
			$this->participants = array();
		}

		$this->participants[] = $participant;

		return $participant->guid;
	}
}

interface DashboardAugmenter
{
	function fillDashboard(Dashboard $dashboard);
}


