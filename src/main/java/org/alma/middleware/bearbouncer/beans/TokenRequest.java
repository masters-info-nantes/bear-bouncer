package org.alma.middleware.bearbouncer.beans;

import java.io.Serializable;

public class TokenRequest implements Serializable {
	private String token;
	
	public TokenRequest(String token) {
		this.token = token;
	}
	
	public TokenRequest() {
		this(null);
	}
	
	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
