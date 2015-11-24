package org.alma.middleware.bearbouncer;

import java.io.Serializable;

public class Identity implements Serializable {
	
	private String firstname;
	private String lastname;
	
	public Identity(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	public Identity() {
		this("","");
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
