package org.alma.middleware.bearbouncer.beans;

import java.io.Serializable;

public class ErrorMsg implements Serializable {
	public String error;
	
	public ErrorMsg(String error) {
		this.error = error;
	}
	
	public ErrorMsg() {
		this(null);
	}
	
	public String getError() {
		return this.error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
}
