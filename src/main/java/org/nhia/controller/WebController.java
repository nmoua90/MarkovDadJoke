package org.nhia.controller;

import java.util.Optional;

import org.nhia.service.MarkovChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Maps Backend Java with Frontend JSP
 * 
 * @author Nhia
 *
 */
@Controller
public class WebController {
	@Autowired
	private MarkovChainService markovChainService;
	private static final int defaultNGram = 1;

	
	@GetMapping("/")
	public String home(@RequestParam("nGramRange") Optional<Integer> nGramInput, Model model) {
		int nGram = nGramInput.isPresent() ? nGramInput.get() : defaultNGram;

		String generatedDadJoke = markovChainService.generateDadJoke(nGram);
		model.addAttribute("dadjoke", generatedDadJoke);
		model.addAttribute("nGramRange", nGram);
		return "index";
	}
	
}
