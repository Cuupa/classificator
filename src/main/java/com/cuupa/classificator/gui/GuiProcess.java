package com.cuupa.classificator.gui;

import java.util.List;

import com.cuupa.classificator.services.kb.semantic.SemanticResult;

public class GuiProcess {

	private String inputText;
	
	private List<SemanticResult> result;

	public String getInputText() {
		return inputText;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public List<SemanticResult> getResult() {
		return result;
	}

	public void setResult(List<SemanticResult> result) {
		this.result = result;
	}
}
