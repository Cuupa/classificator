package com.cuupa.classificator.services.kb;

public class TokenUtil {

	public static Token get(String tokenName) {
		if("oneOf".equals(tokenName)) {
			return new OneOf();
		}
		
		else if("not".equals(tokenName)) {
			return new Not();
		}
		
		else return null;
	}
}
