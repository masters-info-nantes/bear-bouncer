package org.alma.middleware.bearbouncer;

import java.util.UUID;
import javax.json.JsonObject;
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
import org.alma.middleware.bearbouncer.beans.*;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("")
public class MyResource {
	
	private static final String REGEX_IMEI = "[0-9]{15}";
	private static final String REGEX_UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[8-9a-b][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";

	@POST
	@Path("auth")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response auth(IMEIRequest req) {
		Storage db = new Storage();
		
		int status = 200;
		Object res = null;
		if(req.getImei().matches(REGEX_IMEI)) {
			Identity identity = db.getIdentity(req.getImei());
			if(identity != null) {
				status = HttpServletResponse.SC_OK;
				String token = UUID.randomUUID().toString();
				db.putToken(token,req.getImei());
				res = new TokenResponse(token,"sdfd");
			} else {
				status = HttpServletResponse.SC_NOT_FOUND;
				res = new ErrorMsg("No identity corresponds to IMEI '"+req.getImei()+"'");
			}
		} else {
			status = HttpServletResponse.SC_BAD_REQUEST;
			res = new ErrorMsg("Content is not a well formed IMEI.");
		}
		db.close();
		return Response
			.status(status)
			.type(MediaType.APPLICATION_JSON)
			.entity(res)
			.build();
	}

	@POST
	@Path("token")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response token(TokenRequest req) {
		Storage db = new Storage();
		
		int status = 200;
		Object res = null;
		if(req.getToken().matches(REGEX_UUID)) {
			String imei = db.getImei(req.getToken());
			if(imei != null) {
				Identity identity = db.getIdentity(imei);
				status = HttpServletResponse.SC_OK;
				res = identity;
			} else {
				status = HttpServletResponse.SC_NOT_FOUND;
				res = new ErrorMsg("Not a valid or existing token.");
			}
		} else {
			status = HttpServletResponse.SC_BAD_REQUEST;
			res = new ErrorMsg("Content is not a well formed token.");
		}
		db.close();
		return Response
			.status(status)
			.type(MediaType.APPLICATION_JSON)
			.entity(res)
			.build();
	}
}
