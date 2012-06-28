/**

 */

function loadSprintManagementPage() {
	console.log("Loading Sprint Management Page");

	$("#teamList").teamList({
		selected: function(event, data) {
			console.log("Selected team name: " + data.teamName);

			$("#sprintList").sprintList("option", {teamName: data.teamName});
		}
	});

	$("#sprintList").sprintList({
		selected: function(event, data) {
			console.log(data);
			sprint_userstories_list(data.teamName, data.sprintName, function(data) {
				console.log(data);
			})
		}
	});
}