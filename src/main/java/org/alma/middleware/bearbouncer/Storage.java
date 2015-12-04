package org.alma.middleware.bearbouncer;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import org.mapdb.*;
import org.alma.middleware.bearbouncer.beans.Identity;

public class Storage {
	
	private static final String MAP_IDENTITIES = "identities";
	private static final String MAP_TOKENS = "tokens";
	private static final String MAP_CALLBACKS = "callbacks";
	
	private DB db;
	private Map<String,Identity> identities;
	private Map<String,TimeoutString> tokens;
	private Map<String,String> callbacks;
	
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
		if(this.db.exists(MAP_CALLBACKS)) {
			this.callbacks = this.db.treeMap(MAP_CALLBACKS);
		} else {
			this.callbacks = this.db.treeMapCreate(MAP_CALLBACKS)
				.keySerializer(Serializer.STRING)
				.makeOrGet();
		}
		
		// TODO remove following lines
		init();
	}
	
	public void init() {
		this.putIdentity(
			"357870062579664",
			new Identity(
				"John",
				"Doe"
			)
		);
		this.putIdentity(
			"625796643578700",
			new Identity(
				"Jane",
				"Doe"
			)
		);
		this.callbacks.put(
			"bb9dd8fb-98fb-40d7-914a-7ff2cc06cff9",
			"/startSession"
		);
		this.callbacks.put(
			"44bc9103-8ebb-4298-8028-da9fa2902fc0",
			"/startSession"
		);
		this.callbacks.put(
			"80dd9170-01c6-46a4-84e9-f574373842bd",
			"/startSession"
		);
		this.callbacks.put(
			"a8cc715a-f586-4dd6-8de9-f81e7f302841",
			"/startSession"
		);
		this.callbacks.put(
			"5e1f14e4-ac66-46f0-bf1a-a58aa96b758c",
			"/startSession"
		);
		this.callbacks.put(
			"29492a84-7a37-4761-b3d3-0d981d57f248",
			"/startSession"
		);
		this.callbacks.put(
			"78517cd0-1c2e-47af-9599-300ae0e5912b",
			"/startSession"
		);
		this.callbacks.put(
			"d956b2d7-7060-4f7c-a7e2-a5112d1eed2d",
			"/startSession"
		);
		this.callbacks.put(
			"24b3b4f1-51dd-4050-a5be-21846a09b7aa",
			"/startSession"
		);
		this.callbacks.put(
			"32178039-a6df-4ea8-b8fb-bce81b3cc62b",
			"/startSession"
		);
		this.callbacks.put(
			"0bce1317-ea03-4c2c-827c-2e766eb2bd6c",
			"/startSession"
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
		timeoutDate.add(Calendar.MINUTE,5);
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
	
	public String getCallback(String apikey) {
		return this.callbacks.get(apikey);
	}
	
	public String putCallback(String apikey, String callback) {
		return this.callbacks.put(apikey,callback);
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
