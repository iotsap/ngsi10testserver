package com.sap.research.fiware.ngsi10.test.handler;

import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import noNamespace.StatusCode;
import noNamespace.UnsubscribeContextResponse;
import noNamespace.UnsubscribeContextResponseDocument;

import org.apache.xmlbeans.XmlObject;

import com.sap.research.fiware.ngsi10.test.helpers.Constants;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseTestHelper;

public abstract class UnsubscribeContextSubscriptionHandler {

	public final Response handle(String subscriptionIdFromURL, HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		
		RequestTestHelper<? extends XmlObject> reqHlp = initializeRequestHelper(headers, info, requestStr, payload);
		
		// initialize response helper
		ResponseTestHelper<UnsubscribeContextResponseDocument> resHlp = new ResponseTestHelper<UnsubscribeContextResponseDocument>();
		UnsubscribeContextResponseDocument resDoc = UnsubscribeContextResponseDocument.Factory.newInstance();
		UnsubscribeContextResponse resp = resDoc.addNewUnsubscribeContextResponse();
		
		// try to parse request payload
		reqHlp.checkRequest();
		
		String responseSubscriptionId = getSubscriptionIdForResponse(reqHlp, subscriptionIdFromURL);
		
		// set subscriptionId on response
		if (null == responseSubscriptionId) {
			setErrorSubscriptionId(resp);
		} else {
			checkForNotFoundError(responseSubscriptionId, reqHlp);
			resp.setSubscriptionId(responseSubscriptionId);
		}
		
		// handle request errors
		if (reqHlp.hasErrors()) {
			return resHlp.toRestResponse(reqHlp, resDoc);
		}
		
		// build response
		StatusCode statusCode = NgsiStatusCodes.Ok.toStatusCode(reqHlp.getRequestInformation());
		resp.setStatusCode(statusCode);
		
		ArrayList<String> errors = new ArrayList<String>();

		resHlp.isValidResponse(resDoc, errors);
		
		return Response.ok(resDoc.toString()).build();
	}

	protected abstract RequestTestHelper<? extends XmlObject> initializeRequestHelper(HttpHeaders headers, UriInfo info, Request requestStr, String payload);

	protected abstract String getSubscriptionIdForResponse(RequestTestHelper<? extends XmlObject> reqHlp, String subscriptionIdFromURL);

	protected abstract void setErrorSubscriptionId(UnsubscribeContextResponse resp);

	private void checkForNotFoundError(String subscriptionID, RequestTestHelper<? extends XmlObject> reqHlp) {
		if (subscriptionID.contains(Constants.NOT_FOUND)) {
			String details = "Context Subscription with ID \"" + subscriptionID
					+ "\" not found. *You* have provoked this error by"
					+ " specifying a Subscription ID containing the string \""
					+ Constants.NOT_FOUND + "\" in the request. (here: \""
					+ subscriptionID + "\")";
			reqHlp.addNewIssueRecord(getHttpStatusForSubscriptionIdNotFound(), details, NgsiStatusCodes.SubscriptionIdNotFound);
		}
	}

	protected abstract Status getHttpStatusForSubscriptionIdNotFound();

}
