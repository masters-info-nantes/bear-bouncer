package org.alma.middleware.bearbouncer;

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.google.gson.Gson;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("")
public class MyResource {
	
	private static final String REGEX_IMEI = "[0-9]{15}";
	private static final String REGEX_UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[8-9a-b][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";

	@POST
	@Path("auth")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response auth(String imei) {
		Storage db = new Storage();
		
		int status = 200;
		String res = null;
		if(imei.matches(REGEX_IMEI)) {
			Identity identity = db.getIdentity(imei);
			if(identity != null) {
				status = HttpServletResponse.SC_OK;
				res = UUID.randomUUID().toString();
				db.putToken(res,imei);
			} else {
				status = HttpServletResponse.SC_NOT_FOUND;
				res = "No identity corresponds to IMEI '"+imei+"'";
			}
		} else {
			status = HttpServletResponse.SC_BAD_REQUEST;
			res = "Content is not a well formed IMEI.";
		}
		db.close();
		return Response
			.status(status)
			.type(MediaType.TEXT_PLAIN)
			.entity(res)
			.build();
	}

	@POST
	@Path("token")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response token(String token) {
		Storage db = new Storage();
		
		int status = 200;
		String res = null;
		if(token.matches(REGEX_UUID)) {
			String imei = db.getImei(token);
			if(imei != null) {
				Identity identity = db.getIdentity(imei);
				status = HttpServletResponse.SC_OK;
				Gson gson = new Gson();
				res = gson.toJson(identity);
			} else {
				status = HttpServletResponse.SC_NOT_FOUND;
				res = "Not a valid or existing token.";
			}
		} else {
			status = HttpServletResponse.SC_BAD_REQUEST;
			res = "Content is not a well formed token.";
		}
		db.close();
		return Response
			.status(status)
			.type(MediaType.TEXT_PLAIN)
			.entity(res)
			.build();
	}
}
