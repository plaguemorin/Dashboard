/* Author:

 */

// setup self refresh every 10 minutes
setTimeout("location.reload(true);", 10 * 60000);

update();

var lastUpdateTime = 0;

function update()
{
	$.ajax({
		url: "dashboard2.json",
		success: renderData,
		error: function (data)
		{
			// An error occured
		}
	});
}

function renderData(data)
{
	console.log(data);

	updateQuote(data.quote);
	updateSprintData(data.sprintDays);

	//updateActivityLog(data.activityLog);

	lastUpdateTime = data.generatedTime;
	setTimeout("update();", 2 * 60000);
}

function updateSprintData(data) {
	console.log(data);
}

/**
 * Updates the text of the quote
 */
function updateQuote(data)
{
	$("#quote").fadeOut('slow', function()
	{
		$("#quote").text(data);
		$("#quote").fadeIn('slow');
	});
}

function updateActivityLog(data)
{
	var added = 0;

	for (var i in data) {
		var thisItem = data[i];

		if ($("#" + thisItem.guid).length == 0) {
			var $item = singleActivityLog(thisItem);
			$("#activityLog").prepend($item);

			added++;
		}
	}

	console.log("Added " + added + " activity log");
}

function singleActivityLog(al)
{
	var $activityLog = $("<div />");

	var $desc = $("<h3 />");
	$desc.addClass('description');
	$desc.text(al.description);

	var $time = $("<span />");
	$time.addClass('time');
	$time.text((new Date(al.time * 1000)).toDateString());

	var $who = $("<span />");
	$who.addClass("author");
	$who.text(al.who);

	$activityLog.attr("id", al.guid);
	$activityLog.data("time", al.time);
	$activityLog.data("source", al.source);
	$activityLog.addClass("new");
	$activityLog.addClass(al.source);

	$activityLog.append($desc);
	$activityLog.append($time);
	$activityLog.append($who);

	return $activityLog;
}

function populateCi(data)
{
	var $ci = $("<div />");
	$ci.append($("<h2 />").text("Builds"));
	$ci.addClass("ciBuilds");

	for (var i in data) {
		var $build = $("<div />");
		$build.addClass("build");
		$build.text(i);
		$build.addClass(data[i].status);

		$ci.append($build);
	}

	$("#ciBuilds").html($ci);
	setTimeout("updateBuild()", 60000);
}

function populateServerStatus(data)
{
	var $servers = $("<div />").addClass("servers");
	$servers.append($("<h2 />").text("Server Status"));

	for (var serverId in data) {
		var $server = $("<div />").text(serverId);
		if (data[serverId].isRunning) {
			$server.addClass("running");
		} else {
			$server.addClass("down");
		}

		$servers.append($server);
	}

	$("#serverStatus").html($servers);
	setTimeout("updateServerStatus()", 60000);
}

function getStoriesInStatus(data, statusName)
{
	if (data.statues[statusName]) {
		return data.statues[statusName].count;
	}

	return 0;
}

function populateSprintCard(sprintData)
{
	var usdone, ustodo;
	usdone = 0;
	ustodo = 0;

	for (var i in sprintData.userStories) {
		if (sprintData.userStories[i].status == "Verified/Closed") {
			usdone += sprintData.userStories[i].storyPoints;
		} else {
			ustodo += sprintData.userStories[i].storyPoints;
		}
	}

	$("#sprintCardEndDate").text(sprintData.endDate);
	$("#sprintCardDaysLeft").text(sprintData.daysLeft);
	$("#sprintCardNumberSubTasks").text(sprintData.numberTasks);
	$("#sprintCardNumberUserStory").text(sprintData.numberUserStories);
	$("#sprintCardUSDone").text(usdone + "pts");
	$("#sprintCardUSTodo").text(ustodo + "pts");

	$("#sprintCardNumberToDo").text(getStoriesInStatus(sprintData, "Open"));
	$("#sprintCardNumberUnresolved").text(getStoriesInStatus(sprintData, "Unresolved"));
	$("#sprintCardNumberInProgress").text(getStoriesInStatus(sprintData, "In Progress"));
	$("#sprintCardNumberResolved").text(getStoriesInStatus(sprintData, "Resolved"));
	$("#sprintCardNumberDone").text(getStoriesInStatus(sprintData, "Verified/Closed"));
}

function makeSprintGraph(sprintData)
{
	var numTodo = getStoriesInStatus(sprintData, "Open") + getStoriesInStatus(sprintData, "Reopened");
	var numInProgress = getStoriesInStatus(sprintData, "In Progress") + getStoriesInStatus(sprintData, "Resolved");
	var numDone = getStoriesInStatus(sprintData, "Verified/Closed");

	var total = numTodo + numInProgress + numDone;

	var pcentTodo = (numTodo / total) * 100.0;
	var pcentProgress = (numInProgress / total) * 100.0;
	var pcentDone = (numDone / total) * 100.0;

	var $todo = $("<div />").addClass("todo").css("width", pcentTodo + "%").append("To Do");
	var $progress = $("<div />").addClass("progress").css("width", pcentProgress + "%").append("In Progress");
	var $done = $("<div />").addClass("done").css("width", pcentDone + "%").append("Done");

	return $("<div />").addClass("sprintGraph").append($todo).append($progress).append($done);
}

function makeUserStory(userStory)
{
	var $title = $("<h2 />").text(userStory.key);
	var $desc = $("<p />").text(userStory.title);
	var $points = $("<span />").addClass("points").text(userStory.storyPoints);

	var $task = $("<div />").addClass("userStory").append($title).append($desc).append($points);
	if (userStory.status == "Verified/Closed") {
		$task.addClass("done");
	}

	return $task;
}

function doBurnDown(dayData)
{
	var x = [];
	var y = [];
	var y2 = [];
	var y3 = [];

	for (var i in dayData) {
		x.push(x.length);
		y3.push(0);

		if (!dayData[i].isFuture) {
			y.push(dayData[i].numTasksLeft);
		}
	}

	var perDay = y[0] / (x.length - 1);
	for (var w in x) {
		y2.push(y[0] - (perDay * w));
	}

	var sprintLineColor = "rgb(200, 0, 0)";
	if (y2[ y.length - 1 ] > y[y.length - 1]) {
		sprintLineColor = "hsb(0.6,.75, .75)";
	}

	$("svg").remove();
	var $container = $(document);

	var r = Raphael(0, 0, $container.width(), $container.height());
	r.linechart(0, 0,
			$container.width(), $container.height(),
			x,
			[y, y2, y3],
			{
				symbol: "",
				colors: [sprintLineColor, "rgba(255,255,255,.25)", "rgba(255,255,255,.25)"]
			}
	);
	$("svg").css("z-index", "-1000");
}

var genTime = Date.now();
function updateGenTime()
{
	var ago = Math.floor((Date.now() - genTime) / 1000);
	var seconds = ago % 60;
	var minutes = Math.floor(ago / 60);
	var hours = 0;

	$("#lastUpdate").text("Generated " + minutes + " minutes, " + seconds + " seconds ago");

	setTimeout("updateGenTime()", 1000);
}

updateGenTime();

function populateSprintData(data)
{
	$(".sprintName").text(data.name);

	genTime = (data.generatedTime * 1000);

	populateSprintCard(data);

	var sprintGraph = makeSprintGraph(data);
	$("#topSprintGraph").html(sprintGraph);

	var $userStories = $("<div />");
	for (var i in data.userStories) {
		$userStories.append(makeUserStory(data.userStories[i]));
	}

	$("#storiesDash").html($userStories);
	doBurnDown(data.dayGraph);
	setTimeout("updateStories()", 2 * 60000);
}
