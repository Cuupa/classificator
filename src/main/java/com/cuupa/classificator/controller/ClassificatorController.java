package com.cuupa.classificator.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cuupa.classificator.services.Classificiator;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import com.google.gson.Gson;

@RestController
public class ClassificatorController {
	
	private static final Gson gson = new Gson();
	
	private Classificiator classificator;
	
	public ClassificatorController(Classificiator classificator) {
		this.classificator = classificator;
	}

	@RequestMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok().body("200");
	}
	
	@RequestMapping(value = "/classify", method = RequestMethod.POST)
	public ResponseEntity<String> classify(@RequestBody String text) {
		try {
			List<SemanticResult> result = classificator.classifiy(text);
			String semanticResult = gson.toJson(result);
			return ResponseEntity.status(HttpStatus.OK).body(semanticResult);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
