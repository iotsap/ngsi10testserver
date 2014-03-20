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

import com.sap.research.fiware.ngsi10.test.handler.CreateSubscriptionRequestHandler;
import com.sap.research.fiware.ngsi10.test.helpers.MethodNotAllowedHelper;

@Service("subscribeContext")
@Path("/subscribeContext/")
public class SubscribeContext {

	@GET
	public Response postOnSubscribeContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@PUT
	public Response putOnSubscribeContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML })
	public Response subscribeContext(@Context HttpHeaders headers, @Context UriInfo info,
			@Context Request requestStr, String payload) {
		return new CreateSubscriptionRequestHandler().handle(headers, info, requestStr, payload);
	}

	@DELETE
	public Response deleteOnSubscribeContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

}
