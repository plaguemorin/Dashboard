<?php

require_once("dashboardObjects.php");

class QuoteAugmenter implements DashboardAugmenter
{

	private function getQuote() {
		static $quotes = array(
			"All arrays {name} declares are of infinite size, because {name} knows no bounds.",
			"{name} doesn't have disk latency because the hard drive knows to hurry the hell up.",
			"All browsers support the hex definitions #chuck and #norris for the colors black and blue.",
			"{name} can't test for equality because he has no equal.",
			"{name} doesn't need garbage collection because he doesn't call .Dispose(), he calls .DropKick().",
			"{name}'s first program was kill -9.",
			"{name} burst the dot com bubble.",
			"{name} writes code that optimizes itself.",
			"{name} can write infinite recursion functions... and have them return.",
			"{name} can solve the Towers of Hanoi in one move.",
			"The only pattern {name} knows is God Object.",
			"{name} finished World of Warcraft.",
			"Project managers never ask {name} for estimations... ever.",
			"{name} doesn't use web standards as the web will conform to him.",
			"\"It works on my machine\" always holds true for {name}.",
			"Whiteboards are white because {name} scared them that way.",
			"{name}'s beard can type 140 wpm.",
			"{name} can unit test an entire application with a single assert.",
			"{name} doesn't bug hunt as that signifies a probability of failure, he goes bug killing.",
			"{name}'s keyboard doesn't have a Ctrl key because nothing controls {name}.",
			"{name} doesn't need a debugger, he just stares down the bug until the code confesses.",
			"{name} can access private methods.",
			"{name} can instantiate an abstract class.",
			"{name} doesn'tt need to know about class factory pattern. He can instantiate interfaces.",
			"The class object inherits from {name}.",
			"For {name}, NP-Hard = O(1).",
			"{name} knows the last digit of PI.",
			"{name} can divide by zero.",
			"{name} doesn't get compiler errors, the language changes itself to accommodate {name}.",
			"The programs that {name} writes don't have version numbers because he only writes them once. If a user reports a bug or has a feature request they don't live to see the sun set.",
			"{name} doesn't believe in floating point numbers because they can't be typed on his binary keyboard.",
			"{name} solved the Travelling Salesman problem in O(1) time.",
			"{name} never gets a syntax error. Instead, The language gets a DoesNotConformToChuck error.",
			"No statement can catch the ChuckNorrisException.",
			"{name} doesn't program with a keyboard. He stares the computer down until it does what he wants.",
			"{name} doesn't pair program.",
			"{name} can write multi-threaded applications with a single thread.",
			"There is no Esc key on {name}' keyboard, because no one escapes {name}.",
			"{name} doesn't delete files, he blows them away.",
			"{name} can binary search unsorted data.",
			"{name} breaks RSA 128-bit encrypted codes in milliseconds.",
			"{name} went out of an infinite loop.",
			"{name} can read all encrypted data, because nothing can hide from {name}.",
			"{name} hosting is 101% uptime guaranteed.",
			"When a bug sees {name}, it flees screaming in terror, and then immediately self-destructs to avoid being roundhouse-kicked.",
			"{name} rewrote the Google search engine from scratch.",
			"{name} doesn't need the cloud to scale his applications, he uses his laptop.",
			"{name} can access the DB from the UI.",
			"{name} protocol design method has no status, requests or responses, only commands.",
			"{name} programs occupy 150% of CPU, even when they are not executing.",
			"{name} can spawn threads that complete before they are started.",
			"{name} programs do not accept input.",
			"{name} doesn't need an OS.",
			"{name} can compile syntax errors.",
			"{name} compresses his files by doing a flying round house kick to the hard drive.",
			"{name} doesn't use a computer because a computer does everything slower than {name}."
		);

		return $quotes[array_rand($quotes, 1)];
	}

	private function quoteForName($name) {
		$quote = $this->getQuote();

		return str_replace("{name}", $name, $quote);
	}

	function fillDashboard(Dashboard $dashboard)
	{
		$dashboard->quote = $this->quoteForName("Chuck Norris");
	}
}