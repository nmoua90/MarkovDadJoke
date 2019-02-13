package org.nhia.service;

import java.util.ArrayList;
import java.util.List;

import org.nhia.client.DadJokeRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Upon Spring Boot application start-up, this Service automatically runs. It
 * fetches all Dad Jokes on http://icanhazdadjoke.com/ as of 2/12/2019 by
 * retrieving the current max number of pages (18 max pages).
 * 
 * @author Nhia
 *
 */
@Service
public class DadJokeService implements CommandLineRunner {
	@Autowired
	private DadJokeRestClient restClient;
	private List<String> dadJokes;

	/**
	 * Auto-loads Dad joke input texts at Application Start-Up
	 */
	@Override
	public void run(String... args) throws Exception {
		this.dadJokes = new ArrayList<>();
		
		// Fetch 18 pages worth of jokes (roughly, 18*30=540 jokes)
		processDadJokes(18);
	}

	/**
	 * Returns a list of every processed dad joke
	 */
	public List<String> getDadJokes() {
		return dadJokes;
	}

	/**
	 * Takes a total number of pages (containing dad jokes), and inserts each page's
	 * dad jokes into a global list of dad jokes.
	 * 
	 * @param numPages the amount of dad joke pages you want to process through
	 */
	private void processDadJokes(int numPages) {
		for (int i = 0; i < numPages; i++) {
			List<String> curDadJokePage = restClient.getDadJokes(i);
			for (String curDadJoke : curDadJokePage) {
				dadJokes.add(curDadJoke);
			}
		}
	}

}
