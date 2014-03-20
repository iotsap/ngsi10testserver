package com.sap.research.fiware.ngsi10.test.handler;

import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.xmlbeans.GDuration;

import noNamespace.SubscribeError;
import noNamespace.SubscribeResponse;
import noNamespace.UpdateContextSubscriptionRequest;
import noNamespace.UpdateContextSubscriptionRequestDocument;
import noNamespace.UpdateContextSubscriptionResponse;
import noNamespace.UpdateContextSubscriptionResponseDocument;

import com.sap.research.fiware.ngsi10.test.helpers.Constants;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseTestHelper;

public abstract class UpdateContextSubscriptionHandler {

	public final Response handle(String subscriptionIDFromURL, HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		
		// initialize request helper
		RequestTestHelper<UpdateContextSubscriptionRequestDocument> reqHlp = new RequestTestHelper<UpdateContextSubscriptionRequestDocument>(
				requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(true);
		
		// initialize response helper
		ResponseTestHelper<UpdateContextSubscriptionResponseDocument> resHlp = new ResponseTestHelper<UpdateContextSubscriptionResponseDocument>();
		UpdateContextSubscriptionResponseDocument resDoc = UpdateContextSubscriptionResponseDocument.Factory.newInstance();
		UpdateContextSubscriptionResponse resp = resDoc.addNewUpdateContextSubscriptionResponse();
		
		// try to parse request payload
		reqHlp.checkRequest();
		
		// get parsed request from helper
		UpdateContextSubscriptionRequestDocument requestDoc = reqHlp.getReqDoc();
		
		// read subscriptionId from request
		String subscriptionIdFromPayload = null;
		UpdateContextSubscriptionRequest updateContextSubscriptionRequest = null;
		if (null != requestDoc) {
			updateContextSubscriptionRequest = requestDoc.getUpdateContextSubscriptionRequest();
			if (null != updateContextSubscriptionRequest) {
				subscriptionIdFromPayload = updateContextSubscriptionRequest.getSubscriptionId();
			}
		}
		
		String responseSubscriptionID = checkSubscriptionID(subscriptionIDFromURL, reqHlp, subscriptionIdFromPayload);
		
		checkForNotFoundError(reqHlp, subscriptionIDFromURL, subscriptionIdFromPayload);
		
		// handle request errors
		if (reqHlp.hasErrors()) {
			SubscribeError subscribeError = resp.addNewSubscribeError();
			subscribeError.setSubscriptionId(responseSubscriptionID);
			return resHlp.toRestResponse(reqHlp, resDoc);
		}
		
		// build response
		SubscribeResponse subscribeResponse = resp.addNewSubscribeResponse();
		subscribeResponse.setSubscriptionId(responseSubscriptionID);
		
		// reply duration and throttling
		if (null != updateContextSubscriptionRequest) {
			GDuration duration = updateContextSubscriptionRequest.getDuration();
			if (null != duration) {
				subscribeResponse.setDuration(duration);
			}
			GDuration throttling = updateContextSubscriptionRequest.getThrottling();
			if (null != throttling) {
				subscribeResponse.setThrottling(throttling);
			}
		}
		
		ArrayList<String> errors = new ArrayList<String>();
		resHlp.isValidResponse(resDoc, errors);
		
		return Response.ok(resDoc.toString()).build();
	}

	protected abstract String checkSubscriptionID(String subscriptionIDFromURL, RequestTestHelper<UpdateContextSubscriptionRequestDocument> reqHlp, String subscriptionIdFromPayload);

	protected abstract void checkForNotFoundError(RequestTestHelper<UpdateContextSubscriptionRequestDocument> reqHlp, String subscriptionIDFromURL, String subscriptionIdFromPayload);

	protected void checkForNotFoundError(RequestTestHelper<UpdateContextSubscriptionRequestDocument> reqHlp, String subscriptionID, Status status) {
		if (null == subscriptionID) {
			return;
		}
		if (subscriptionID.contains(Constants.NOT_FOUND)) {
			String details = "Context Subscription with ID \"" + subscriptionID
					+ "\" not found. *You* have provoked this error by"
					+ " specifying a Subscription ID containing the string \""
					+ Constants.NOT_FOUND + "\" in the request. (here: \""
					+ subscriptionID + "\")";
			reqHlp.addNewIssueRecord(status, details, NgsiStatusCodes.SubscriptionIdNotFound);
		}
	}

}
