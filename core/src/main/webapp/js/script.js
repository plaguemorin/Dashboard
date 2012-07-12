/**
 * List all the available teams
 *
 * @param {Function} successCallback
 */
function team_list(successCallback) {
	console.log("[Text] Getting Team List");

	$.ajax({
		url: "/api/teams/",
		contentType:"application/xml",
		method: "GET",
		dataType: "xml",
		success: function(data) {
			successCallback($(data));
		},
		complete: function(jqXHR, textStatus) {
			console.log("[Teams] Done with status: " + textStatus);
		}
	});
}

/**
 * List all sprints for a team
 *
 * @param {String} teamName
 * @param {Function} successCallback
 */
function sprint_list(teamName, successCallback) {
	$.ajax({
		url: "/api/teams/" + encodeURIComponent(teamName) + "/",
		contentType:"application/xml",
		method: "GET",
		dataType: "xml",
		success: function(data) {
			successCallback($(data));
		},

		error: function() {

		}
	});
}

/**
 * Gets a sprints
 *
 * @param {String} teamName
 * @param {String} sprintName
 * @param {Function} successCallback
 */
function sprint_get(teamName, sprintName, successCallback) {
	$.ajax({
		url: "/api/teams/" + encodeURIComponent(teamName) + "/" + encodeURIComponent(sprintName) + "/",
		contentType:"application/xml",
		method: "GET",
		dataType: "xml",
		success: function(data) {
			successCallback($(data));
		},

		error: function() {

		}
	});
}

/**
 * Creates a sprint for a team
 *
 * @param {String} teamName
 * @param {String} sprintName
 * @param {Function} successCallback
 */
function sprint_create(teamName, sprintName, successCallback) {
	$.ajax({
		url: "/api/teams/" + encodeURIComponent(teamName) + "/" + encodeURIComponent(sprintName) + "/",
		contentType:"application/xml",
		method: "POST",
		dataType: "xml",
		success: function(data) {
			successCallback(teamName, sprintName);
		},

		error: function() {

		}
	});
}

function sprint_userstories_list(teamName, sprintName, successCallback) {
	$.ajax({
		url: "/api/teams/" + encodeURIComponent(teamName) + "/" + encodeURIComponent(sprintName) + "/userstories/",
		contentType:"application/xml",
		method: "GET",
		dataType: "xml",
		success: function(data) {
			successCallback($(data));
		},

		error: function() {

		}
	});

}

// the widget definition, where "custom" is the namespace,
// "colorize" the widget name
$.widget("custom.teamList", {
	// default options
	options: {
		selelectTeam: null,
		okButton: null,
		newButton: null,

		// callbacks
		change: null
	},

	// the constructor
	_create: function() {
		this.element
			// add a class for theming
				.addClass("custom-team-list")
			// prevent double click to select text
				.disableSelection();

		this.selelectTeam = $("<select>", {"class": "custom-team-list-select"}).appendTo(this.element).select();
		this.okButton = $("<button></button>", {"class": "custom-team-list-ok"}).appendTo(this.element).text("Ok").button();

		// in 1.9 would use this._bind( this.changer, { change: "teamList" });
		var that = this;
		this.selelectTeam.bind("change.teamList", function() {
			// _bind would handle this check
			if (that.options.disabled) {
				return;
			}

		});

		this.okButton.bind("click.teamList", function(event) {
			if (that.options.disabled) {
				return;
			}

			that._trigger("selected", event, {"teamName": that.selelectTeam.val()});
			return false;
		});

		this._refresh();
	},

	_updateOptions: function(teamData) {
		var that = this;
		this.selelectTeam.remove("option");
		teamData.find("team").each(function() {
			that.selelectTeam.append($("<option></option>")
											 .attr("value", $(this).text())
											 .text($(this).text()));
		});

		this._trigger("change");
	},

	// called when created, and later when changing options
	_refresh: function() {
		var that = this;
		team_list(function(data) {
			that._updateOptions(data);
		});

	},

	// events bound via _bind are removed automatically
	// revert other modifications here
	_destroy: function() {
		// remove generated elements
		this.selelectTeam.remove();
		this.okButton.remove();

		this.element
				.removeClass("custom-team-list")
				.enableSelection();
	},

	// _setOptions is called with a hash of all options that are changing
	// always refresh when changing options
	_setOptions: function() {
		// in 1.9 would use _superApply
		$.Widget.prototype._setOptions.apply(this, arguments);
		this._refresh();
	},

	// _setOption is called for each individual option that is changing
	_setOption: function(key, value) {
		// in 1.9 would use _super
		$.Widget.prototype._setOption.call(this, key, value);
	}
});


// the widget definition, where "custom" is the namespace,
// "colorize" the widget name
$.widget("custom.sprintList", {
	// default options
	options: {
		selelectSprint: null,
		okButton: null,
		newButton: null,
		teamName: null,

		// callbacks
		change: null
	},

	// the constructor
	_create: function() {
		this.element
			// add a class for theming
				.addClass("custom-sprint-list")
			// prevent double click to select text
				.disableSelection();

		this.selelectSprint = $("<select>", {"class": "custom-sprint-list-select"}).appendTo(this.element).select();
		this.okButton = $("<button></button>", {"class": "custom-sprint-list-ok"}).appendTo(this.element).text("Ok").button();

		// in 1.9 would use this._bind( this.changer, { change: "teamList" });
		var that = this;
		this.selelectSprint.bind("change.sprintList", function() {
			// _bind would handle this check
			if (that.options.disabled) {
				return;
			}
		});

		this.okButton.bind("click.sprintList", function(event) {
			if (that.options.disabled) {
				return;
			}

			that._trigger("selected", event, {"sprintName": that.selelectSprint.val(), "teamName": that.options.teamName});
			return false;
		});

		this._refresh();
	},

	_updateOptions: function(teamData) {
		var that = this;
		this.selelectSprint.remove("option");
		teamData.find("sprint").each(function() {
			console.log(this);
			that.selelectSprint.append($("<option></option>")
											   .attr("value", $(this).find("sprintName").text())
											   .text($(this).find("sprintName").text()))
					;
		});

		this._trigger("change");
	},

	// called when created, and later when changing options
	_refresh: function() {
		if (this.options.teamName == null) {
			return;
		}

		var that = this;
		sprint_list(this.options.teamName, function(data) {
			that._updateOptions(data);
		});

	},

	// events bound via _bind are removed automatically
	// revert other modifications here
	_destroy: function() {
		// remove generated elements
		this.selelectTeam.remove();
		this.okButton.remove();

		this.element
				.removeClass("custom-sprint-list")
				.enableSelection();
	},

	// _setOptions is called with a hash of all options that are changing
	// always refresh when changing options
	_setOptions: function() {
		// in 1.9 would use _superApply
		$.Widget.prototype._setOptions.apply(this, arguments);
		this._refresh();
	},

	// _setOption is called for each individual option that is changing
	_setOption: function(key, value) {
		// in 1.9 would use _super
		$.Widget.prototype._setOption.call(this, key, value);
	}
});


// ---------------------
// -- Page Navigation --
// ---------------------
$(".navbar .nav a").click(function() {
	$("div[role='main']").hide();
	$(".navbar .nav li").removeClass("active");
	$(this).parent().addClass("active");

	var $pageDiv = $("div[role='main']" + $(this).attr('href'));
	$pageDiv.show();

	if ($pageDiv.data('onLoad')) {
		window[$pageDiv.data('onLoad')]();
	}

	return false;
});

// Cheap way to select default page
$("div[role='main']").hide();
$(".navbar .nav a.default").click();
