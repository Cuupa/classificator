package com.cuupa.classificator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cuupa.classificator.services.Classificiator;
import com.cuupa.classificator.to.Document;
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
	
	@RequestMapping("/classify")
	public ResponseEntity<String> classify(@RequestBody String json) {
		try {
			Document document = gson.fromJson(json, Document.class);
			document = classificator.classify(document);
		} catch(Exception e) {
			
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
