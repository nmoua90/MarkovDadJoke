package org.nhia.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Maps a JSON object for a single Dad Joke retrieved from a URI GET Request
 * (https://icanhazdadjoke.com)
 * 
 * @author Nhia
 *
 */
@Data
@JsonIgnoreProperties
public class DadJoke {
	private String id;
	private String joke;
}