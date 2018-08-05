package com.cuupa.classificator.services.kb.semantic.token;

public class TokenUtil {

	public static Token get(String tokenName) {
		if("oneOf".equals(tokenName)) {
			return new OneOf();
		}
		
		else if("not".equals(tokenName)) {
			return new Not();
		}
		
		else if("all".equals(tokenName)) {
			return new All();
		}
		
		else return null;
	}
}
