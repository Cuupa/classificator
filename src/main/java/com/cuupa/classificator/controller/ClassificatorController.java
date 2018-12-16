package com.cuupa.classificator.controller;

import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClassificatorController {
	
	private static final Gson gson = new Gson();

    private Classificator classificator;

    public ClassificatorController(Classificator classificator) {
		this.classificator = classificator;
	}

	@RequestMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok().body("200");
	}

    @RequestMapping(value = "/classifyText", method = RequestMethod.POST)
	public ResponseEntity<String> classify(@RequestBody String text) {
		try {
            List<SemanticResult> result = classificator.classify(text);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(result));
		} catch(Exception e) {
            e.printStackTrace();
        }
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

    @RequestMapping(value = "/classify", method = RequestMethod.POST)
    public ResponseEntity<String> classify(@RequestBody byte[] content) {
        try {
            List<SemanticResult> result = classificator.classify(content);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
