<?php

class JiraConnection {
	private $url;
	private $user;
	private $pass;

	private $connection;
	private $sessionKey;

	public function __construct($url, $user, $pass) {
		$this->url = $url;
		$this->user = $user;
		$this->pass = $pass;

		$this->connection = null;
		$this->sessionKey = null;
	}

	public function __destory() {
		$this->logout();
	}

	public function logout() {
		if ($this->sessionKey != null && $this->connection != null) {
			$this->connection->logout($this->sessionKey);
		}
		$this->sessionKey = null;
	}

	public function login() {
		if ($this->sessionKey == null) {
			$this->connect();
			$this->sessionKey = $this->connection->login($this->user, $this->pass);
		}
	}

	protected function connect() {
		if ($this->connection == null) {
			$this->connection = new SoapClient($this->url);
		}
	}

	public function getVersions($name) {
		$this->login();
		return $this->connection->getVersions($this->sessionKey, $name);
	}

	public function getIssuesFromJqlSearch($jql) {
		$this->login();
		return $this->connection->getIssuesFromJqlSearch($this->sessionKey, $jql, 9999);
	}

	public function getStatuses() {
		$this->login();
		return $this->connection->getStatuses($this->sessionKey);
	}

	public function getResolutionDateById($id) {
		$this->login();
		return $this->connection->getResolutionDateById($this->sessionKey, $id);
	}

	public function getUser($param1)
	{
		$this->login();
		return $this->connection->getUser($this->sessionKey, $param1);
	}

}
