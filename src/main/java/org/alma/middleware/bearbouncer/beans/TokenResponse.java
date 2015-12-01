package org.alma.middleware.bearbouncer.beans;

import java.io.Serializable;

public class TokenResponse implements Serializable {
	public String token;
	public String callback;
	
	public TokenResponse(String token, String callback) {
		this.token = token;
		this.callback = callback;
	}
	
	public TokenResponse() {
		this(null,null);
	}
	
	public String getToken() {
		return this.token;
	}
	
	public String getCallback() {
		return this.callback;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setCallback(String callback) {
		this.callback = callback;
	}
}
