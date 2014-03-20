package com.sap.research.fiware.ngsi10.test.handler;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import noNamespace.UnsubscribeContextResponse;

import org.apache.xmlbeans.XmlObject;

import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;

public class ConvenienceUnsubscribeContextSubscriptionHandler extends UnsubscribeContextSubscriptionHandler {

	@Override
	protected RequestTestHelper<? extends XmlObject> initializeRequestHelper(HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		RequestTestHelper<XmlObject> reqHlp = new RequestTestHelper<XmlObject>(requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(false);
		return reqHlp;
	}

	@Override
	protected String getSubscriptionIdForResponse(RequestTestHelper<? extends XmlObject> reqHlp, String subscriptionIdFromURL) {
		return subscriptionIdFromURL;
	}

	@Override
	protected void setErrorSubscriptionId(UnsubscribeContextResponse resp) {
		// never reached
	}

	@Override
	protected Status getHttpStatusForSubscriptionIdNotFound() {
		return Status.NOT_FOUND;
	}

}
