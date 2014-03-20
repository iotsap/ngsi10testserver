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

import com.sap.research.fiware.ngsi10.test.handler.StandardQueryContextHandler;
import com.sap.research.fiware.ngsi10.test.helpers.MethodNotAllowedHelper;

@Service("queryContext")
@Path("/queryContext/")
public class QueryContext {

	@GET
	public Response postOnQueryContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@PUT
	public Response putOnQueryContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML })
	public Response queryContext(@Context HttpHeaders headers, @Context UriInfo info,
			@Context Request requestStr, String payload) {
		return new StandardQueryContextHandler(headers, info, requestStr, payload).handle();
	}

	@DELETE
	public Response deleteOnQueryContext() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

}
