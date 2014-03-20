package com.sap.research.fiware.ngsi10.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

@Service("rootResource")
@Path("/")
public class NGSI10RootResource {

	@GET
	@Produces("text/plain")
	public Response postOnQueryContext() {
		return Response.ok("Welcome to the NGSI10 Test Server!").build();
	}

}
