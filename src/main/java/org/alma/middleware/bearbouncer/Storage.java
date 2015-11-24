package org.alma.middleware.bearbouncer;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import org.mapdb.*;

public class Storage {
	
	private static final String MAP_IDENTITIES = "identities";
	private static final String MAP_TOKENS = "tokens";
	
	private DB db;
	private Map<String,Identity> identities;
	private Map<String,TimeoutString> tokens;
	
	public Storage() {
		this.db = DBMaker.fileDB(new File("storage.db"))
			.closeOnJvmShutdown()
			.transactionDisable()// no need to commit to save
			.make();
		
		if(this.db.exists(MAP_IDENTITIES)) {
			this.identities = this.db.treeMap(MAP_IDENTITIES);
		} else {
			this.identities = this.db.treeMapCreate(MAP_IDENTITIES)
				.keySerializer(Serializer.STRING)
				.makeOrGet();
		}
		if(this.db.exists(MAP_TOKENS)) {
			this.tokens = this.db.treeMap(MAP_TOKENS);
		} else {
			this.tokens = this.db.treeMapCreate(MAP_TOKENS)
				.keySerializer(Serializer.STRING)
				.makeOrGet();
		}
		
		// TODO remove following lines
		this.putIdentity(
			"357870062579664",
			new Identity(
				"John",
				"Doe"
			)
		);
	}
	
	public Identity getIdentity(String imei) {
		return this.identities.get(imei);
	}
	
	public Identity putIdentity(String imei, Identity identity) {
		return this.identities.put(imei,identity);
	}
	
	public String getImei(String token) {
		TimeoutString str = this.tokens.get(token);
		if(str != null) {
			Calendar now = Calendar.getInstance();//.getTimeInMillis()
			Calendar timeoutDate = Calendar.getInstance();
			timeoutDate.setTimeInMillis(str.timeoutDate);//.getTimeInMillis()
			if(now.before(timeoutDate)) {
				return str.value;
			} else {
				return null;
			}
		}
		return null;
	}
	
	public String putToken(String token, String imei) {
		Calendar now = Calendar.getInstance();
		Calendar timeoutDate = Calendar.getInstance();
		timeoutDate.add(Calendar.MINUTE,1);
		TimeoutString str = new TimeoutString(imei,timeoutDate.getTimeInMillis());
		TimeoutString previousValue = this.tokens.put(token,str);
		if(previousValue != null) {
			Calendar previousTimeoutDate = Calendar.getInstance();
			previousTimeoutDate.setTimeInMillis(previousValue.timeoutDate);
			if(now.before(previousTimeoutDate)) {
				return previousValue.value;
			} else {
				return null;
			}
		}
		return null;
	}
	
	public void close() {
		this.db.close();
	}
	
	private static class TimeoutString implements Serializable {
		String value;
		long timeoutDate;
		
		public TimeoutString(String value, long timeoutDate) {
			this.value = value;
			this.timeoutDate = timeoutDate;
		}
	}
	
}
