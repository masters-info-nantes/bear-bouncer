package org.alma.middleware.bearbouncer.beans;

import java.io.Serializable;

public class IMEIRequest implements Serializable {
	private String imei;
	private String apikey;
	
	public IMEIRequest(String imei, String apikey) {
		this.imei = imei;
		this.apikey = apikey;
	}
	
	public IMEIRequest() {
		this(null,null);
	}
	
	public String getImei() {
		return this.imei;
	}
	
	public String getApikey() {
		return this.apikey;
	}
	
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
}
