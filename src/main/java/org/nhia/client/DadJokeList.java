package org.nhia.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Maps a JSON array of Dad Jokes retrieved from a URI GET request
 * (https://icanhazdadjoke.com/search?page={pageNum}&limit=30)
 * 
 * @author Nhia
 *
 */
@Data
@JsonIgnoreProperties
public class DadJokeList {
	private int current_page;
	private int limit;
	private int next_page;
	private DadJoke[] results;
	private String search_term;
	private int status;
	private int total_jokes;
	private int total_pages;
}
