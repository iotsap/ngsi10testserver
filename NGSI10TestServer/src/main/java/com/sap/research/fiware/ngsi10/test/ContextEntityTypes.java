package com.sap.research.fiware.ngsi10.test;

/**
 * This class is is dealing with request for context entities.
 */

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Service;

import com.sap.research.fiware.ngsi10.test.handler.ConvenienceQueryContextHandler;
import com.sap.research.fiware.ngsi10.test.helpers.MethodNotAllowedHelper;

@Service("contextEntityTypes")
@Path("/contextEntityTypes/")
public class ContextEntityTypes {
	
	@GET
	@Path("{typeName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getContextEntityTypesByTypeName(@PathParam("typeName") String typeName,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request requestStr, String payload) {
		return handleTypeNameRequest(typeName, null, null, headers, info, requestStr, payload);
	}

	private Response handleTypeNameRequest(String typeName, String attributeName, String attributeDomainName, HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		return new ConvenienceQueryContextHandler(typeName, attributeName, attributeDomainName, headers, info, requestStr, payload).handle();
	}

	@PUT
	@Path("{typeName}")
	public Response putOnContextEntityTypeName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@POST
	@Path("{typeName}")
	public Response postOnContextEntityTypeName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@DELETE
	@Path("{typeName}")
	public Response deleteOnContextEntityTypeName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	
	@GET
	@Path("{typeName}/attributes")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getContextEntityTypesByTypeNameAttributes(@PathParam("typeName") String typeName,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request requestStr, String payload) {
		return handleTypeNameRequest(typeName, null, null, headers, info, requestStr, payload);
	}
	
	@PUT
	@Path("{typeName}/attributes")
	public Response putOnContextEntityTypeNameAttributes() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@POST
	@Path("{typeName}/attributes")
	public Response postOnContextEntityTypeNameAttributes() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@DELETE
	@Path("{typeName}/attributes")
	public Response deleteOnContextEntityTypeNameAttributes() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	
	@GET
	@Path("{typeName}/attributes/{attributeName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getContextEntityTypesByTypeNameAndAttributeName(@PathParam("typeName") String typeName, @PathParam("attributeName") String attributeName,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request requestStr, String payload) {
		return handleTypeNameRequest(typeName, attributeName, null, headers, info, requestStr, payload);	}
	
	@PUT
	@Path("{typeName}/attributes/{attributeName}")
	public Response putOnContextEntityTypeAttributeName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@POST
	@Path("{typeName}/attributes/{attributeName}")
	public Response postOnContextEntityTypeAttributeName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@DELETE
	@Path("{typeName}/attributes/{attributeName}")
	public Response deleteOnContextEntityTypeAttributeName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	
	@GET
	@Path("{typeName}/attributeDomains/{attributeDomainName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response getContextEntityTypesByTypeNameAndAttributeDomainName(@PathParam("typeName") String typeName, @PathParam("attributeDomainName") String attributeDomainName,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request requestStr, String payload) {
		return handleTypeNameRequest(typeName, null, attributeDomainName, headers, info, requestStr, payload);
	}
	
	@PUT
	@Path("{typeName}/attributeDomains/{attributeDomainName}")
	public Response putOnContextEntityTypeAttributeDomainName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@POST
	@Path("{typeName}/attributeDomains/{attributeDomainName}")
	public Response postOnContextEntityTypeAttributeDomainName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
	@DELETE
	@Path("{typeName}/attributeDomains/{attributeDomainName}")
	public Response deleteOnContextEntityTypeAttributeDomainName() {
		return MethodNotAllowedHelper.onlyGetAllowed();
	}
	
}
