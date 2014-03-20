package com.sap.research.fiware.ngsi10.test;

/**
 * Server implementation for FiWare NGSI 9/10 Testing.
 *   
 * This class is is dealing with requests for context entities, http://{serverRoot}/{apiVersion}/contextEntities/{entityID}
 * 
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import noNamespace.AppendContextElementRequestDocument;
import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextAttributeResponse;
import noNamespace.ContextAttributeResponseDocument;
import noNamespace.ContextElementResponseDocument;
import noNamespace.StatusCodeDocument;
import noNamespace.UpdateContextAttributeRequestDocument;
import noNamespace.UpdateContextElementRequestDocument;
import noNamespace.UpdateContextElementResponseDocument;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.springframework.stereotype.Service;

import com.sap.research.fiware.ngsi10.test.helpers.Constants;
import com.sap.research.fiware.ngsi10.test.helpers.MethodNotAllowedHelper;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseGenHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseTestHelper;

@Service("contextEntities")
@Path("/contextEntities/")
public class ContextEntities {

	private Logger logger = Logger.getLogger(this.getClass());

	/*
	 * Convenience Function Resources -- Individual Context Entity
	 * 
	 * http://{serverRoot}/{apiVersion}/contextEntities/{entityID}
	 * 
	 * GET
	 * 
	 * http://localhost:8080/NGSIRestInterface/NGSI10/contextEntities/4321
	 */

	@GET
	@Path("{entityId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntities_GET(@PathParam("entityId") String entityId, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(request, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setRequiresPayload(false);
		reqHlp.setAllowsQueryParams(false);

		// GET requests

		// initialize response helper
		ResponseTestHelper<ContextElementResponseDocument> resHlp = new ResponseTestHelper<ContextElementResponseDocument>();
		ContextElementResponseDocument resDoc = ContextElementResponseDocument.Factory.newInstance();

		// add entityId to the response
		resDoc.addNewContextElementResponse().addNewContextElement().addNewEntityId().setId(entityId);

		// parse request and check for generic errors
		reqHlp.checkRequest();
		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		//
		// 3rd pass: (requires parsing and understanding the request and
		// constructing a specific, request-specific reply)
		//
		// column H: request with .ERROR results in not found
		// column I: POST request with ".EXISTING" in URL or ID "existing"
		// results in 409 conflict
		// column L: 400 request that violates further constraints mentioned in
		// the specification results in 400 bad request

		// check for and handle NOT FOUND error
		// reqHlp.checkForNotFoundError(entityId);

		// Construct positive reply
		ContextElementResponseDocument response = ResponseGenHelper
				.generateStandardContextElementResponseDocument(entityId);

		return resHlp.toRestResponse(reqHlp, response);

	} // end contextEntities_GET()

	/**
	 * Convenience Function Resources -- Individual Context Entity
	 * 
	 * http://{serverRoot}/{apiVersion}/contextEntities/{entityID}
	 * 
	 * PUT
	 * 
	 * http://localhost:8080/NGSIRestInterface/NGSI10/contextEntities/4321
	 * 
	 * @param request
	 *            of type UpdateContextElementRequest
	 * 
	 * @return a StatusCodeDocument or a ContextAttributeResponse in a Response
	 * 
	 */

	@PUT
	@Path("{entityId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntities_PUT(@PathParam("entityId") String entityId, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request requestStr, String payload) {

		// initialize request helper
		RequestTestHelper<UpdateContextElementRequestDocument> reqHlp = new RequestTestHelper<UpdateContextElementRequestDocument>(
				requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(true);

		// initialize response helper
		ResponseTestHelper<UpdateContextElementResponseDocument> resHlp = new ResponseTestHelper<UpdateContextElementResponseDocument>();
		UpdateContextElementResponseDocument resDoc = UpdateContextElementResponseDocument.Factory.newInstance();

		// parse request payload and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		//
		// 3rd pass: (requires parsing and understanding the request and
		// constructing a specific, request-specific reply)
		//
		// column H: request with .ERROR results in not found
		// column I: POST request with ".EXISTING" in URL or ID "existing"
		// results in 409 conflict
		// column L: 400 request that violates further constraints mentioned in
		// the specification results in 400 bad request

		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		resDoc = ResponseGenHelper.generateUpdateContextElementResponseDocument(reqHlp);

		Response response = resHlp.toRestResponse(reqHlp, resDoc);

		// System.out.println("\n\n XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n");
		// System.out.println(resDoc.xmlText(Constants.ppOpts));
		// System.out.println("\n\n XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n");

		return response;

	} // end contextEntities_PUT()

	public static void main(String... args) {

		UpdateContextElementRequestDocument reqDoc = ResponseGenHelper
				.generateStandardUpdateContextElementRequestDocument();

		// delete the ContextAttributeList to check error handling
		// reqDoc.getUpdateContextElementRequest().setContextAttributeList(null);
		System.out.println(reqDoc.xmlText(Constants.ppOpts));

		// Helper stuff

		RequestTestHelper<UpdateContextElementRequestDocument> reqHlp = new RequestTestHelper<UpdateContextElementRequestDocument>(
				null, null, null, reqDoc.xmlText());
		reqHlp.setRequiredMediaType(null);
		reqHlp.setRequiresPayload(true);
		reqHlp.setAllowsQueryParams(false);

		reqHlp.checkRequest();
		System.out.println(reqHlp.getRequestInformation());

		// generate Response from Request
		UpdateContextElementResponseDocument resDoc = ResponseGenHelper
				.generateUpdateContextElementResponseDocument(reqHlp);
		// System.out.println(resDoc.xmlText(Constants.ppOpts));

		// ... for response
		ResponseTestHelper<UpdateContextElementResponseDocument> resHlp = new ResponseTestHelper<UpdateContextElementResponseDocument>();
		Response response = resHlp.toRestResponse(reqHlp, resDoc);

		System.out.println(response.toString());
		System.out.println("\n\n XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n");
		System.out.println(resDoc.xmlText(Constants.ppOpts));
		System.out.println("\n\n XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n");

	} // end main()

	/*
	 * Convenience Function Resources -- Individual Context Entity
	 * 
	 * http://{serverRoot}/{apiVersion}/contextEntities/{entityID}
	 * 
	 * POST
	 * 
	 * http://localhost:8080/NGSIRestInterface/NGSI10/contextEntities/4321
	 */

	@POST
	@Path("{entityId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntities_POST(@PathParam("entityId") String entityId, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<AppendContextElementRequestDocument> reqHlp = new RequestTestHelper<AppendContextElementRequestDocument>(
				request, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(true);

		// initialize response helper

		// XXX: AppendContextElementResponse is not specified in
		// Ngsi10_Operations_v07.xsd
		// ResponseTestHelper<AppendContextElementResponseDocument> resHlp = new
		// ResponseTestHelper<AppendContextElementResponseDocument>();
		// AppendContextElementResponseDocument resDoc =
		// AppendContextElementResponseDocument.Factory.newInstance();

		ResponseTestHelper<StatusCodeDocument> resHlp = new ResponseTestHelper<StatusCodeDocument>();
		StatusCodeDocument resDoc = StatusCodeDocument.Factory.newInstance();

		// try to parse request payload
		reqHlp.checkRequest();

		// handle request errors
		if (reqHlp.hasErrors()) {
			return resHlp.toRestResponse(reqHlp, resDoc);
		}

		// check for and handle EXISTING/ CONFLICT error
		reqHlp.checkForExistingError(entityId);

		// XXX: this is always an internal server error... the response class is
		// missing :)
		String errStr = "The response to this operation (i.e., AppendContextElementResponse) "
				+ "is not specified in the FIWARE NGSI10 binding (Ngsi10_Operations_v07.xsd).";
		reqHlp.addNewIssueRecord(Status.INTERNAL_SERVER_ERROR, errStr, NgsiStatusCodes.ReceiverInternalError);

		return resHlp.toRestResponse(reqHlp, resDoc);

		// return
		// Response.status(Status.INTERNAL_SERVER_ERROR).entity(errStr).build();
	} // end contextEntities_POST()

	/*
	 * http://{serverRoot}/{apiVersion}/contextEntities/{entityID}
	 * 
	 * DELETE
	 * 
	 * http://localhost:8080/NGSIRestInterface/NGSI10/contextEntities/4321
	 */

	@DELETE
	@Path("{entityId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntities_DELETE(@PathParam("entityId") String entityId, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(request, info, headers, payload);
		reqHlp.setRequiresPayload(true);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<StatusCodeDocument> resHlp = new ResponseTestHelper<StatusCodeDocument>();
		StatusCodeDocument resDoc = StatusCodeDocument.Factory.newInstance();

		// parse request payload and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		Response response = resHlp.toRestResponse(reqHlp, resDoc);
		return response;

	} // end contextEntities_DELETE()

	/*
	 * 
	 * {entityId}/attributes
	 * 
	 * 
	 * same as parent resource
	 */

	@GET
	@Path("{entityId}/attributes")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributes_GET(@PathParam("entityId") String entityId, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {
		return contextEntityAttributes_GET(entityId, headers, info, request, payload);
	}

	@PUT
	@Path("{entityId}/attributes")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributes_PUT(@PathParam("entityId") String entityId, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {
		return contextEntityAttributes_PUT(entityId, headers, info, request, payload);
	}

	@POST
	@Path("{entityId}/attributes")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributes_POST(@PathParam("entityId") String entityId, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {
		return contextEntityAttributes_POST(entityId, headers, info, request, payload);
	}

	@DELETE
	@Path("{entityId}/attributes")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributes_DELETE(@PathParam("entityId") String entityId,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request request, String payload) {
		return contextEntityAttributes_DELETE(entityId, headers, info, request, payload);
	}

	/*
	 * 
	 * http://{serverRoot}/{apiVersion}/contextEntities/{entityID}/attributes/{
	 * attributeName}
	 * 
	 * 3.4 Resource: Attribute of Individual Context Entity
	 * 
	 * Instances of this resource are used for retrieving all available
	 * information about a specific attribute of the context entity represented
	 * by the parent resource via GET and adding information via POST.
	 * Furthermore, requests to delete all available attribute values can be
	 * issued using DELETE.
	 */

	@GET
	@Path("{entityId}/attributes/{attributeName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeName_GET(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @Context HttpHeaders headers, @Context UriInfo info,
			@Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(request, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setRequiresPayload(false);
		// GET requests
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<ContextAttributeResponseDocument> resHlp = new ResponseTestHelper<ContextAttributeResponseDocument>();
		ContextAttributeResponseDocument resDoc = ContextAttributeResponseDocument.Factory.newInstance();

		// parse request and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		// complete the response doc to create a positive response
		ContextAttributeResponse res = resDoc.addNewContextAttributeResponse();

		ContextAttributeList contextAttributeList = res.addNewContextAttributeList();
		ContextAttribute ctxAttrib = contextAttributeList.addNewContextAttribute();
		ResponseGenHelper.fillSampleContextAttribute(ctxAttrib, 0);

		Response response = resHlp.toRestResponse(reqHlp, resDoc);
		return response;

	} // end contextEntityAttributeName_GET

	@PUT
	@Path("{entityId}/attributes/{attributeName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeName_PUT(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @Context HttpHeaders headers, @Context UriInfo info,
			@Context Request request, String payload) {

		return MethodNotAllowedHelper.onlyGetPostDeleteAllowed();
	} // end contextEntityAttributeName_PUT

	@POST
	@Path("{entityId}/attributes/{attributeName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeName_POST(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @Context HttpHeaders headers, @Context UriInfo info,
			@Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<UpdateContextAttributeRequestDocument> reqHlp = new RequestTestHelper<UpdateContextAttributeRequestDocument>(
				request, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setRequiresPayload(true);
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<StatusCodeDocument> resHlp = new ResponseTestHelper<StatusCodeDocument>();
		StatusCodeDocument resDoc = StatusCodeDocument.Factory.newInstance();

		// XXX add entityId to response?

		// parse request payload and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			// XXX can't we delay tjat to the end of the method to have only one
			// "return"
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for (and handle) EXISTING error
		reqHlp.checkForExistingError(entityId);

		// get parsed request from helper
		UpdateContextAttributeRequestDocument reqDoc = reqHlp.getReqDoc();

		// XXX check for ID in form of metadata: The ID of the new value
		// instance can be provided in the input message in the form of a
		// Metadata instance with Name field “ID”. In this case, another value
		// instance with the same ID must not yet exist; otherwise the creation
		// of the new value instance SHALL fail.

		if (null != reqDoc) {
			// XXX create positive response
		}

		Response response = resHlp.toRestResponse(reqHlp, resDoc);

		return response;
	} // end contextEntityAttributeName_POST

	@DELETE
	@Path("{entityId}/attributes/{attributeName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeName_DELETE(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @Context HttpHeaders headers, @Context UriInfo info,
			@Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(request, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setRequiresPayload(false);
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<StatusCodeDocument> resHlp = new ResponseTestHelper<StatusCodeDocument>();
		StatusCodeDocument resDoc = StatusCodeDocument.Factory.newInstance();

		// XXX add entityId to response

		// parse request payload (if any) and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for (and handle) not-found error
		reqHlp.checkForNotFoundError(entityId);

		// XXX check for ID in form of metadata

		// XXX create positive response in resDoc

		Response response = resHlp.toRestResponse(reqHlp, resDoc);

		return response;
	} // end contextEntityAttributeName_DELETE

	/*
	 * 
	 * 
	 * {entityId}/attributes/{attributeName}/{valueId}
	 * 
	 * 
	 * .
	 */

	@GET
	@Path("{entityId}/attributes/{attributeName}/{valueId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeNameValueid_GET(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @PathParam("valueId") String valueId,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(request, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setRequiresPayload(false);
        
		// GET requests
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<ContextAttributeResponseDocument> resHlp = new ResponseTestHelper<ContextAttributeResponseDocument>();
		ContextAttributeResponseDocument resDoc = ContextAttributeResponseDocument.Factory.newInstance();

		// parse request and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		// complete the response doc to create a positive response
		ContextAttributeResponse res = resDoc.addNewContextAttributeResponse();
		// XXX: check: this list must only contain one ContextAttribute element
		ContextAttributeList contextAttributeList = res.addNewContextAttributeList();
		ContextAttribute ctxAttrib = contextAttributeList.addNewContextAttribute();

		ResponseGenHelper.fillSampleContextAttribute(ctxAttrib, 0);
		ctxAttrib.setName(attributeName); // needs to have same name as in
											// request
		ResponseGenHelper.addIdAsMetadata(ctxAttrib, valueId);

		Response response = resHlp.toRestResponse(reqHlp, resDoc);
		return response;

	} // end contextEntityAttributeNameValueid_GET()

	@PUT
	@Path("{entityId}/attributes/{attributeName}/{valueId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeNameValueid_PUT(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @PathParam("valueId") String valueId,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<UpdateContextAttributeRequestDocument> reqHlp = new RequestTestHelper<UpdateContextAttributeRequestDocument>(
				request, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setRequiresPayload(true);
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<StatusCodeDocument> resHlp = new ResponseTestHelper<StatusCodeDocument>();
		StatusCodeDocument resDoc = StatusCodeDocument.Factory.newInstance();

		// parse request and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		// get parsed request from helper
		UpdateContextAttributeRequestDocument reqDoc = reqHlp.getReqDoc();

		// XXX check the request for constraints:
		// This operation can only be
		// used on resources that represent already existing attribute value
		// instances. It cannot be used for creating new value instances. The
		// latter can instead be achieved by a POST on the parent resource.
		//
		// The request body is an instance of updateContextAttributeRequest. In
		// case this instance contains a ContextMetadata instance with Name
		// “ID”, the Value must correspond to the {valueID} part of the access
		// URI, or else the server SHALL return an error.

		if (null != reqDoc) {
			// XXX create a positive response
		}
		// complete the response doc to create a positive response

		Response response = resHlp.toRestResponse(reqHlp, resDoc);
		return response;
	} // end contextEntityAttributeNameValueid_PUT()

	@POST
	@Path("{entityId}/attributes/{attributeName}/{valueId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeNameValueid_POST(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @PathParam("valueId") String valueId,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request request, String payload) {

		// only GET, PUT, and DELETE allowed
		return MethodNotAllowedHelper.onlyGetPutDeleteAllowed();
	} // end contextEntityAttributeNameValueid_POST()

	@DELETE
	@Path("{entityId}/attributes/{attributeName}/{valueId}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeNameValueid_DELETE(@PathParam("entityId") String entityId,
			@PathParam("attributeName") String attributeName, @PathParam("valueId") String valueId,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request request, String payload) {

		// initialize request helper
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(request, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setRequiresPayload(false);
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<StatusCodeDocument> resHlp = new ResponseTestHelper<StatusCodeDocument>();
		StatusCodeDocument resDoc = StatusCodeDocument.Factory.newInstance();

		// parse request and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		// XXX check the request for constraints

		// XXX create a positive response
		Response response = resHlp.toRestResponse(reqHlp, resDoc);
		return response;
	} // end contextEntityAttributeNameValueid_DELETE()

	/*
	 * http://{serverRoot}/{apiVersion}/contextEntities/{EntityID}/attributeDomains
	 * /{attributeDomainName}
	 * 
	 * 
	 * Instances of this resource are used for retrieving all available
	 * information about the attributes in the domain represented by the
	 * resource. Other operations shall not be supported on this resource type.
	 */

	@GET
	@Path("{EntityID}/attributeDomains/{attributeDomainName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeDomainName_GET(@PathParam("entityId") String entityId,
			@PathParam("attributeDomainName") String attributeName, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {

		//
		// initialize request helper
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(request, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setRequiresPayload(false);
		// GET requests
		reqHlp.setAllowsQueryParams(false);

		// initialize response helper
		ResponseTestHelper<ContextAttributeResponseDocument> resHlp = new ResponseTestHelper<ContextAttributeResponseDocument>();
		ContextAttributeResponseDocument resDoc = ContextAttributeResponseDocument.Factory.newInstance();

		// parse request and check for generic errors
		reqHlp.checkRequest();
		if (reqHlp.hasErrors()) {
			// ... and handle them
			return resHlp.toRestResponse(reqHlp, resDoc);
		} // end if

		// check for and handle NOT FOUND error
		reqHlp.checkForNotFoundError(entityId);

		// XXX is this a correct response? where is the attributeDomainName
		// stored?

		// complete the response doc to create a positive response
		ContextAttributeResponse res = resDoc.addNewContextAttributeResponse();
		ContextAttributeList contextAttributeList = res.addNewContextAttributeList();
		ContextAttribute ctxAttrib = contextAttributeList.addNewContextAttribute();
		ResponseGenHelper.fillSampleContextAttribute(ctxAttrib, 0);
		ctxAttrib.setName(attributeName);

		Response response = resHlp.toRestResponse(reqHlp, resDoc);
		return response;

	} // end contextEntityAttributeDomainName_GET()

	@PUT
	@Path("{EntityID}/attributeDomains/{attributeDomainName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeDomainName_PUT(@PathParam("entityId") String entityId,
			@PathParam("attributeDomainName") String attributeName, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {

		return MethodNotAllowedHelper.onlyGetAllowed();
	} // end contextEntityAttributeDomainName_PUT()

	@POST
	@Path("{EntityID}/attributeDomains/{attributeDomainName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeDomainName_POST(@PathParam("entityId") String entityId,
			@PathParam("attributeDomainName") String attributeName, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {

		return MethodNotAllowedHelper.onlyGetAllowed();
	} // end contextEntityAttributeDomainName_POST()

	@DELETE
	@Path("{EntityID}/attributeDomains/{attributeDomainName}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response contextEntityAttributeDomainName_DELETE(@PathParam("entityId") String entityId,
			@PathParam("attributeDomainName") String attributeName, @Context HttpHeaders headers,
			@Context UriInfo info, @Context Request request, String payload) {

		return MethodNotAllowedHelper.onlyGetAllowed();
	} // end contextEntityAttributeDomainName_DELETE()

} // end class ContextEntities
