package com.sap.research.fiware.ngsi10.test.handler;

import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import noNamespace.AttributeList;
import noNamespace.EntityId;
import noNamespace.EntityIdList;
import noNamespace.SubscribeContextRequest;
import noNamespace.SubscribeContextRequestDocument;
import noNamespace.SubscribeContextResponse;
import noNamespace.SubscribeContextResponseDocument;
import noNamespace.SubscribeResponse;

import org.apache.xmlbeans.GDuration;

import com.sap.research.fiware.ngsi10.test.helpers.Constants;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseTestHelper;

public class CreateSubscriptionRequestHandler {

	public Response handle(HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		// initialize request helper
		RequestTestHelper<SubscribeContextRequestDocument> reqHlp = new RequestTestHelper<SubscribeContextRequestDocument>(
				requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(true);
		
		// initialize response helper
		ResponseTestHelper<SubscribeContextResponseDocument> resHlp = new ResponseTestHelper<SubscribeContextResponseDocument>();
		SubscribeContextResponseDocument resDoc = SubscribeContextResponseDocument.Factory.newInstance();
		
		// try to parse request payload
		reqHlp.checkRequest();
		
		if (!reqHlp.hasErrors()) {
			SubscribeContextRequestDocument requestDoc = reqHlp.getReqDoc();
			SubscribeContextRequest req = requestDoc.getSubscribeContextRequest();
			// check for entity id not found
			EntityIdList entityIdList = req.getEntityIdList();
			EntityId[] entityIds = entityIdList.getEntityIdArray();
			for (int i = 0; i < entityIds.length; i++) {
				EntityId entityId = entityIds[i];
				String id = entityId.getId();
				if (id.contains(Constants.NOT_FOUND)) {
					String details = "Context entity with id \"" + id
							+ "\" not found. *You* have provoked this error by"
							+ " specifying an id containing the string \""
							+ Constants.NOT_FOUND + "\" in the request. (here: \""
							+ id + "\")";
					reqHlp.addNewIssueRecord(Status.OK, details, NgsiStatusCodes.ContextElementNotFound);
				}
			}
			// check for attribute not found
			AttributeList attributeList = req.getAttributeList();
			String[] attributes = attributeList.getAttributeArray();
			for (int i = 0; i < attributes.length; i++) {
				String attribute = attributes[i];
				if (attribute.contains(Constants.NOT_FOUND)) {
					String details = "Context entity with attribute \"" + attribute
							+ "\" not found. *You* have provoked this error by"
							+ " specifying an id containing the string \""
							+ Constants.NOT_FOUND + "\" in the request. (here: \""
							+ attribute + "\")";
					reqHlp.addNewIssueRecord(Status.OK, details, NgsiStatusCodes.ContextElementNotFound);
				}
			}
		}
		
		// handle request errors
		if (reqHlp.hasErrors()) {
			return resHlp.toRestResponse(reqHlp, resDoc);
		}
		
		// get parsed request from helper
		SubscribeContextRequestDocument requestDoc = reqHlp.getReqDoc();
		
		// build response
		SubscribeContextResponseDocument respDoc = SubscribeContextResponseDocument.Factory.newInstance();
		SubscribeContextResponse resp = respDoc.addNewSubscribeContextResponse();
		SubscribeResponse subscribeResponse = resp.addNewSubscribeResponse();
		subscribeResponse.setSubscriptionId("42");
		
		// reply duration and throttling
		SubscribeContextRequest subscribeContextRequest = requestDoc.getSubscribeContextRequest();
		if (null != subscribeContextRequest) {
			GDuration duration = subscribeContextRequest.getDuration();
			if (null != duration) {
				subscribeResponse.setDuration(duration);
			}
			GDuration throttling = subscribeContextRequest.getThrottling();
			if (null != throttling) {
				subscribeResponse.setThrottling(throttling);
			}
		}
		
		ArrayList<String> errors = new ArrayList<String>();
        
		resHlp.isValidResponse(respDoc, errors);
		
		return Response.ok(respDoc.toString()).build();
	}

}
