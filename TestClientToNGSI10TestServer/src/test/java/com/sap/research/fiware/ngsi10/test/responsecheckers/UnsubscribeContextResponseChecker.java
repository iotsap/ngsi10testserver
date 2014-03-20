package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import java.io.InputStream;

import noNamespace.StatusCode;
import noNamespace.UnsubscribeContextResponse;
import noNamespace.UnsubscribeContextResponseDocument;

public class UnsubscribeContextResponseChecker extends ResponseChecker {

	UnsubscribeContextResponseChecker() {
	}

	@Override
	protected void readPayloadFrom(InputStream inputStream) throws Exception {
		responseDocument = UnsubscribeContextResponseDocument.Factory.parse(inputStream);
	}

	@Override
	protected void extractResponse() {
		response = ((UnsubscribeContextResponseDocument) responseDocument).getUnsubscribeContextResponse();
	}

	@Override
	protected void assertContent() {
		UnsubscribeContextResponse unsubscribeContextResponse = (UnsubscribeContextResponse) response;
		String actualID = unsubscribeContextResponse.getSubscriptionId();
		assertNotNull(actualID);
		assertEquals(expectedID, actualID);
		StatusCode actualStatusCode = unsubscribeContextResponse.getStatusCode();
		assertNotNull(actualStatusCode);
		assertEquals(expectedNGSIStatusCode, actualStatusCode.getCode());
		assertEquals(expectedNGSIReasonPhrase, actualStatusCode.getReasonPhrase());
		String statusCodeDetails = actualStatusCode.getDetails().toString();
		if (null != expectedMessages) {
			for (String expectedMessage : expectedMessages) {
				assertTrue(statusCodeDetails.contains(expectedMessage));
			}
		}
		if (null != expectedErrorMessages) {
			for (String expectedErrorMessage : expectedErrorMessages) {
				assertTrue(statusCodeDetails.contains(expectedErrorMessage));
			}
		}
	}

}
