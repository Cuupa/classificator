package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import java.util.regex.Pattern;

public abstract class Extract {


	public abstract Pattern getPattern();

	public abstract String normalize(String value);
}
