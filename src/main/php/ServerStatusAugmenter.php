<?php

require_once("dashboardObjects.php");

class ServerStatusAugmenter implements DashboardAugmenter
{

	private $serverHome = "/home/tomcat7/hybris-services/";

	private $fullHome = null;
	private $serverName;
	private $isValid = false;

	public function setInstanceName($name) {
		$instancePath = $this->serverHome . $name;
		$this->serverName = $name;
		$this->isValid = false;

		// Make sure we have no . or .. in path
		if (strpos($instancePath, ".") !== false) {
			return;
		}

		// Make sure it's a directory structure we know
		if (!file_exists($this->fullHome . "/bin/startup.sh") || !is_dir($this->fullHome . "/bin")) {
			return;
		}

		$this->fullHome = $instancePath;
		$this->isValid = true;
	}

	function fillDashboard(Dashboard $dashboard)
	{
		$serverState = new ServerStatus();
		
		if ($this->isValid) {
			$serverState->serverName = $this->serverName;
			$serverState->isOk = $this->isRunning();
		} else {
			$serverState->serverName = $this->serverName . " (Invalid)";
			$serverState->isOk = false;
		}

		$dashboard->serverStatus[] = $serverState;
	}

	private function getPid()
	{
		if (file_exists($this->fullHome . "/tomcat.pid")) {
			return trim(file_get_contents($this->fullHome . "/tomcat.pid"));
		}

		return 0;
	}

	private function isRunning()
	{
		$pid = $this->getPid();
		if ($pid == 0) {
			return false;
		}

		return file_exists("/proc/$pid");
	}

}