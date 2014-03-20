package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import java.io.InputStream;

import noNamespace.SubscribeContextResponse;
import noNamespace.SubscribeContextResponseDocument;

public class SubscribeContextResponseChecker extends ContextSubscriptionResponseChecker {

	private static final String SET_EXPECTED_ID_ERROR = "You should not know the expected ID returned with a subscribe operation!";

	SubscribeContextResponseChecker() {
	}

	@Override
	void setExpectedID(String expectedID) {
		throw new RuntimeException(SET_EXPECTED_ID_ERROR);
	}

	@Override
	protected void readPayloadFrom(InputStream inputStream) throws Exception {
		responseDocument = SubscribeContextResponseDocument.Factory.parse(inputStream);
	}

	@Override
	protected void extractResponse() {
		response = ((SubscribeContextResponseDocument) responseDocument).getSubscribeContextResponse();
	}

	@Override
	protected void extractSubscribeResponse() {
		subscribeResponse = ((SubscribeContextResponse) response).getSubscribeResponse();
	}

	@Override
	protected void extractSubscribeError() {
		subscribeError = ((SubscribeContextResponse) response).getSubscribeError();
	}

	@Override
	protected void checkForExpectedIDIsUnset(String actualSubscriptionId) {
		assertNotEquals("", actualSubscriptionId);
	}

	@Override
	protected void checkForExpectedIDIsSet(String actualSubscriptionId) {
		fail(SET_EXPECTED_ID_ERROR);
	}

	@Override
	protected void checkErrorSubscriptionId(String actualSubscriptionId) {
		assertNull(actualSubscriptionId);
	}

}
