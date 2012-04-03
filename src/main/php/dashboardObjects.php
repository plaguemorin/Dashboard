<?php

class Burndown
{
	public $numberToDo;
	public $numberInProgress;
	public $numberDone;
}

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

class SprintDay {
	/**
	 * @var int
	 */
	public $dayNum;

	/**
	 * @var int
	 */
	public $date;

	/**
	 * @var Burndown
	 */
	public $burnDown;
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
}

interface DashboardAugmenter
{
	function fillDashboard(Dashboard $dashboard);
}


