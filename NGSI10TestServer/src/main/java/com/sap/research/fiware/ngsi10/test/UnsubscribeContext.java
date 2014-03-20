package com.sap.research.fiware.ngsi10.test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Service;

import com.sap.research.fiware.ngsi10.test.handler.StandardUnsubscribeContextSubscriptionHandler;
import com.sap.research.fiware.ngsi10.test.helpers.MethodNotAllowedHelper;

@Service("unsubscribeContext")
@Path("/unsubscribeContext/")
public class UnsubscribeContext {

	@GET
	public Response postOnUnsubscribeContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@PUT
	public Response putOnUnsubscribeContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML })
	public Response unsubscribeContext(@Context HttpHeaders headers, @Context UriInfo info,
			@Context Request requestStr, String payload) {
		
		return new StandardUnsubscribeContextSubscriptionHandler().handle(null, headers, info, requestStr, payload);
	}

	@DELETE
	public Response deleteOnUnsubscribeContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

}
