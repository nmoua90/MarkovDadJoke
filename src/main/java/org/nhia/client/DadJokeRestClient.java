package org.nhia.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Consumes icanhazdadjoke.com's RESTFUL API for dad jokes.
 * 
 * @author Nhia
 *
 */
@Component
public class DadJokeRestClient {

	/**
	 * Retrieves 30 Dad Jokes from icanhazdadjoke.com based on the website's ordered
	 * paginated jokes.
	 * 
	 * @param pageNum the page number where the 30 Dad Jokes will be fetched from
	 * @return a list containing 30 Dad Jokes
	 */
	public List<String> getDadJokes(int pageNum) {
		String uri = "https://icanhazdadjoke.com/search?page=" + pageNum + "&limit=30";

		List<String> dadJokesList = new ArrayList<>();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent", "Chrome/54.0.2840.99");
		headers.set("accept", MediaType.APPLICATION_JSON_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<DadJokeList> result = restTemplate.exchange(uri, HttpMethod.GET, entity, DadJokeList.class);

		DadJoke[] dadJokes = result.getBody().getResults();
		for (DadJoke curJokeContainer : dadJokes) {
			dadJokesList.add(curJokeContainer.getJoke());
		}

		return dadJokesList;
	}

}
