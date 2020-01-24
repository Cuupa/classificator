package com.cuupa.classificator.services.kb.semantic;

public class Metadata {

	private final String name;
	private final String value;

	public Metadata(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
