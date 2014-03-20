package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import java.io.InputStream;

import noNamespace.UpdateContextSubscriptionResponse;
import noNamespace.UpdateContextSubscriptionResponseDocument;

public class UpdateContextSubscriptionResponseChecker extends ContextSubscriptionResponseChecker {

	UpdateContextSubscriptionResponseChecker() {
	}

	@Override
	protected void readPayloadFrom(InputStream inputStream) throws Exception {
		responseDocument = UpdateContextSubscriptionResponseDocument.Factory.parse(inputStream);
	}

	@Override
	protected void extractResponse() {
		response = ((UpdateContextSubscriptionResponseDocument) responseDocument).getUpdateContextSubscriptionResponse();
	}

	@Override
	protected void extractSubscribeResponse() {
		subscribeResponse = ((UpdateContextSubscriptionResponse) response).getSubscribeResponse();
	}

	@Override
	protected void extractSubscribeError() {
		subscribeError = ((UpdateContextSubscriptionResponse) response).getSubscribeError();
	}

	@Override
	protected void checkForExpectedIDIsUnset(String actualSubscriptionId) {
		assertNull(actualSubscriptionId);
	}

	@Override
	protected void checkForExpectedIDIsSet(String actualSubscriptionId) {
		assertNotNull(actualSubscriptionId);
		assertEquals(expectedID, actualSubscriptionId);
	}

	@Override
	protected void checkErrorSubscriptionId(String actualSubscriptionId) {
		if (null == expectedID) {
			checkForExpectedIDIsUnset(actualSubscriptionId);
		} else {
			checkForExpectedIDIsSet(actualSubscriptionId);
		}
	}

}
