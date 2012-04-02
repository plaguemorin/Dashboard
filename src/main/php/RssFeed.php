<?php

require_once("dashboardObjects.php");

class RssFeedAugmenter implements DashboardAugmenter
{
	private $url;
	private $prefix = '';

	private $feedSource;

	public function setFeedUrl($url) {
		$this->url = $url;
	}

	function fillDashboard(Dashboard $dashboard)
	{
		$rssContent = file_get_contents($this->url);
		$rssXml = simplexml_load_string($rssContent);

		foreach ($rssXml->channel as $channel) {
			$this->feedSource = (string) $channel->title;
			
			foreach ($channel->item as $item) {
				$ac = $this->buildActivityLogForItem($item);
				$dashboard->activityLog[] = $ac;
			}
		}
	}

	/**
	 * @param $item
	 * @return ActivityLogItem
	 */
	private function buildActivityLogForItem($item)
	{
		$ret = new ActivityLogItem();

		$ret->who = (string) $item->author;
		$ret->description = $this->prefix . (string) $item->title;
		$ret->time = strtotime((string) $item->pubDate);
		$ret->source = $this->sanitizeGuid($this->feedSource);
		$ret->pointsDelta = 0;
		$ret->guid = $this->sanitizeGuid((string) $item->guid);

		return $ret;
	}

	private function sanitizeGuid($inputGuid) 
	{
		return preg_replace('/[^A-Za-z0-9]/', '', (string) $inputGuid);
	}

	public function setPrefix($string1)
	{
		$this->prefix = $string1;
	}
}
