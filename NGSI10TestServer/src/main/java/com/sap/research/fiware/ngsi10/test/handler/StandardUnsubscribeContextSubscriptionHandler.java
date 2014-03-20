package com.sap.research.fiware.ngsi10.test.handler;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import noNamespace.UnsubscribeContextRequest;
import noNamespace.UnsubscribeContextRequestDocument;
import noNamespace.UnsubscribeContextResponse;

import org.apache.xmlbeans.XmlObject;

import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;

public class StandardUnsubscribeContextSubscriptionHandler extends UnsubscribeContextSubscriptionHandler {

	@Override
	protected RequestTestHelper<? extends XmlObject> initializeRequestHelper(HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		RequestTestHelper<UnsubscribeContextRequestDocument> reqHlp = new RequestTestHelper<UnsubscribeContextRequestDocument>(requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(true);
		return reqHlp;
	}

	@Override
	protected String getSubscriptionIdForResponse(RequestTestHelper<? extends XmlObject> reqHlp, String subscriptionIdFromURL) {
		// get parsed request from helper
		UnsubscribeContextRequestDocument requestDoc = (UnsubscribeContextRequestDocument) reqHlp.getReqDoc();
		
		// read subscriptionId from request and write it to response
		String subscriptionIdFromPayload = null;
		if (null != requestDoc) {
			UnsubscribeContextRequest unsubscribeContextRequest = requestDoc.getUnsubscribeContextRequest();
			if (null != unsubscribeContextRequest) {
				subscriptionIdFromPayload = unsubscribeContextRequest.getSubscriptionId();
			}
		}
		
		return subscriptionIdFromPayload;
	}

	@Override
	protected void setErrorSubscriptionId(UnsubscribeContextResponse resp) {
		resp.setSubscriptionId("UnsubscribeContextRequest contains no subscription ID.");
	}

	@Override
	protected Status getHttpStatusForSubscriptionIdNotFound() {
		return Status.OK;
	}

}
