package com.sap.research.fiware.ngsi10.test.handler;

import javax.ws.rs.core.Response.Status;

import noNamespace.UpdateContextSubscriptionRequestDocument;

import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;

public class StandardUpdateContextSubscriptionHandler extends UpdateContextSubscriptionHandler {

	@Override
	protected String checkSubscriptionID(String subscriptionIDFromURL, RequestTestHelper<UpdateContextSubscriptionRequestDocument> reqHlp, String subscriptionIdFromPayload) {
		String responseSubscriptionID = subscriptionIdFromPayload;
		if (null == subscriptionIdFromPayload) {
			String description = "UpdateContextSubscriptionRequest contains no subscription ID.";
			reqHlp.addNewIssueRecord(Status.BAD_REQUEST, description, NgsiStatusCodes.BadRequest);
			responseSubscriptionID = description;
		}
		return responseSubscriptionID;
	}

	@Override
	protected void checkForNotFoundError(RequestTestHelper<UpdateContextSubscriptionRequestDocument> reqHlp, String subscriptionIDFromURL, String subscriptionIdFromPayload) {
		checkForNotFoundError(reqHlp, subscriptionIdFromPayload, Status.OK);
	}

}
