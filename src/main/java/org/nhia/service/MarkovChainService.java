package org.nhia.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Generates new dad jokes based on existing dad jokes.
 * 
 * @author Nhia
 *
 */
@Service
public class MarkovChainService {
	@Autowired
	private DadJokeService dadJokeService;
	private static final Random rand = new Random();
	private List<String> rawDadJokeData;
	private int curDadJokeId;

	// Key = the current state, value = list of possible future states
	private Map<String, List<String>> markovChainDataTable;

	// Key = UID, value = Key value from the HashMap above
	private Map<Integer, String> idLookupTable;

	public MarkovChainService() {
		this.markovChainDataTable = new HashMap<>();
		this.idLookupTable = new HashMap<>();
	}

	/**
	 * Generates a new dad joke based on existing dad jokes via a Markov Chain.
	 * Generates text on the Word level.
	 * 
	 * @param nGram the number of words that the Markov Chain should break the
	 *              original text by
	 * @return a generated dad joke
	 */
	public String generateDadJoke(int nGram) {
		// Get all dad jokes, then split on the WORD level
		rawDadJokeData = dadJokeService.getDadJokes();
		
		for (String joke : rawDadJokeData) {
			String[] jokeParsed = joke.split(" ");

			fillHashMaps(jokeParsed, nGram);
		}

		// Start generating Dad Joke
		String generatedDadJoke = "";
		String currentState = generateStartingState(nGram);
		String nextState;
		
		while (!endsWithTerminatingCharacter(currentState)) {
			generatedDadJoke += currentState;
			
			List<String> nextStates = markovChainDataTable.get(currentState);
			if (nextStates != null) {
				nextState = generateNextState(nextStates);
			// If you reach an unnatural ending point, start over...
			} else {
				StringBuilder revisedDadJoke = new StringBuilder(generatedDadJoke);
				revisedDadJoke.setCharAt(revisedDadJoke.length()-1, '.');
				generatedDadJoke = revisedDadJoke.toString() + " ";
				nextState = generateStartingState(nGram);
			}
			currentState = nextState;
		}
		generatedDadJoke += currentState;
	
		return generatedDadJoke;
	}

	/**
	 * Fills both the Markov Chain Data Table and Lookup Table with data needed to
	 * generate a random Markov Chain dad joke.
	 * 
	 * @param arrayOfWords a dad joke that has been broken up at the word level
	 * @param nGram        the number of words that the Markov Chain should
	 *                     partition by
	 */
	private void fillHashMaps(String[] arrayOfWords, int nGram) {
		for (int i = 0; i < arrayOfWords.length; i++) {
			int nextStartingIndex = i + nGram;

			// Handle edge-case where next starting index is out-of-bounds
			if (nextStartingIndex >= arrayOfWords.length) {
				break;
			}

			String[] currentStateValues = Arrays.copyOfRange(arrayOfWords, i, nextStartingIndex);
			// Concatenate current state values
			String curState = "";
			for (String curSubWord : currentStateValues) {
				if (curSubWord != null) {
					curState += curSubWord + " ";
				}
			}

			String[] curStatesFutureState = Arrays.copyOfRange(arrayOfWords, nextStartingIndex,
					(nextStartingIndex + nGram));
			// Concatenate possible following state values
			String futureState = "";
			for (String curSubWord : curStatesFutureState) {
				if (curSubWord != null) {
					futureState += curSubWord + " ";
				}
			}

			// Add to ID Lookup Table if current State is being seen for first time
			if (!markovChainDataTable.containsKey(curState)) {
				idLookupTable.put(curDadJokeId, curState);
				curDadJokeId++;
			}

			// Prepare INSERT/UPDATE to referencing state list within Markov Chain
			// Table
			List<String> curStateListOfPossibleFutureStates;
			if (!markovChainDataTable.containsKey(curState)) {
				curStateListOfPossibleFutureStates = new ArrayList<>(); // INSERT
			} else {
				curStateListOfPossibleFutureStates = markovChainDataTable.get(curState); // UPDATE
			}

			// INSERT or UPDATE into Markov Chain Table
			curStateListOfPossibleFutureStates.add(futureState);
			markovChainDataTable.put(curState, curStateListOfPossibleFutureStates);
		}
	} // end of fillHashMaps();
	
	/**
	 * Randomly generates a starting state for the Markov Chain, with the following
	 * restrictions that the starting state must: (1) Be of length nGrams, (2) Start
	 * with a capital letter.
	 * 
	 * @param nGram the number of words the starting state must contain
	 * @return a randomly selected starting state
	 */
	private String generateStartingState(int nGram) {
		int randNum;
		String startingState;
		String[] startingStateWords;
		String firstWord;

		do {
			randNum = rand.nextInt(idLookupTable.size());

			startingState = idLookupTable.get(randNum);
			startingStateWords = startingState.split(" ");
			firstWord = startingStateWords[0];

		} while (nGram != startingStateWords.length || !wordIsCaptalized(firstWord));

		return startingState;
	}
	
	/**
	 * Generates the next state for the Markov Chain to continue to.
	 * 
	 * @param startState the current state
	 * @return the next state
	 */
	private String generateNextState(List<String> nextStates) {
		int randNum = rand.nextInt(nextStates.size());
		return nextStates.get(randNum);
	}

	/**
	 * Checks if word starts with capital letter
	 */
	private boolean wordIsCaptalized(String word) {
		if(word.length()>0) {
			return Character.isUpperCase(word.charAt(0));
		}
		return false;
	}

	/**
	 * Checks if a word ends with terminating character
	 */
	private boolean endsWithTerminatingCharacter(String word) {
		char lastChar = (word.length() < 2) ? ' ' : word.charAt(word.length() - 2);
		List<Character> endingQualifiers = Arrays.asList('.','!','\"','”','?');
		return endingQualifiers.contains(lastChar);
	}

}