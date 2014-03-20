package com.sap.research.fiware.ngsi10.test;

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

import noNamespace.UnsubscribeContextResponseDocument;
import noNamespace.UpdateContextSubscriptionRequestDocument;
import noNamespace.UpdateContextSubscriptionResponseDocument;

import org.apache.xmlbeans.XmlObject;
import org.springframework.stereotype.Service;

import com.sap.research.fiware.ngsi10.test.handler.ConvenienceUnsubscribeContextSubscriptionHandler;
import com.sap.research.fiware.ngsi10.test.handler.ConvenienceUpdateContextSubscriptionHandler;
import com.sap.research.fiware.ngsi10.test.handler.CreateSubscriptionRequestHandler;
import com.sap.research.fiware.ngsi10.test.helpers.MethodNotAllowedHelper;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseTestHelper;

@Service("contextSubscriptions")
@Path("/contextSubscriptions")
public class ContextSubscriptions {

	@GET
	public Response getOnSubscriptions() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@PUT
	public Response putOnSubscriptions() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML })
	public Response createNewSubscription(@Context HttpHeaders headers, @Context UriInfo info,
			@Context Request requestStr, String payload) {
		return new CreateSubscriptionRequestHandler().handle(headers, info, requestStr, payload);
	}

	@DELETE
	public Response deleteOnSubscriptions() {
		return MethodNotAllowedHelper.onlyPostAllowed();
	}

	@GET
	@Path("/{subscriptionID}")
	public Response getOnSubscriptionID(@PathParam("subscriptionID") String subscriptionID) {
		return MethodNotAllowedHelper.onlyPutDeleteAllowed();
	}

	@PUT
	@Path("/{subscriptionID}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response updateSubscription(@PathParam("subscriptionID") String subscriptionIDFromURL,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request requestStr, String payload) {
		
		if (null == subscriptionIDFromURL) {
			// this should never happen -> internal server error
			RequestTestHelper<UpdateContextSubscriptionRequestDocument> reqHlp = new RequestTestHelper<UpdateContextSubscriptionRequestDocument>(
					requestStr, info, headers, payload);
			ResponseTestHelper<UpdateContextSubscriptionResponseDocument> resHlp = new ResponseTestHelper<UpdateContextSubscriptionResponseDocument>();
			UpdateContextSubscriptionResponseDocument resDoc = UpdateContextSubscriptionResponseDocument.Factory.newInstance();
			String description = "SubscriptionID in URL should never be null.";
			reqHlp.addNewIssueRecord(Status.INTERNAL_SERVER_ERROR, description, NgsiStatusCodes.ReceiverInternalError);
			return resHlp.toRestResponse(reqHlp, resDoc);
		}
		
		return new ConvenienceUpdateContextSubscriptionHandler().handle(subscriptionIDFromURL, headers, info, requestStr, payload);
	}

	@POST
	@Path("/{subscriptionID}")
	public Response postOnSubscriptionID(@PathParam("subscriptionID") String subscriptionID) {
		return MethodNotAllowedHelper.onlyPutDeleteAllowed();
	}

	@DELETE
	@Path("/{subscriptionID}")
	@Produces({ MediaType.APPLICATION_XML })
	public Response cancelSubscription(@PathParam("subscriptionID") String subscriptionIdFromURL,
			@Context HttpHeaders headers, @Context UriInfo info, @Context Request requestStr, String payload) {
		
		if (null == subscriptionIdFromURL) {
			// this should never happen -> internal server error
			RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(requestStr, info, headers, payload);
			ResponseTestHelper<UnsubscribeContextResponseDocument> resHlp = new ResponseTestHelper<UnsubscribeContextResponseDocument>();
			UnsubscribeContextResponseDocument resDoc = UnsubscribeContextResponseDocument.Factory.newInstance();
			String description = "SubscriptionID in URL should never be null.";
			reqHlp.addNewIssueRecord(Status.INTERNAL_SERVER_ERROR, description, NgsiStatusCodes.ReceiverInternalError);
			return resHlp.toRestResponse(reqHlp, resDoc);
		}
		
		return new ConvenienceUnsubscribeContextSubscriptionHandler().handle(subscriptionIdFromURL, headers, info, requestStr, payload);
	}

}
