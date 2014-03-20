package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import noNamespace.StatusCode;
import noNamespace.SubscribeError;
import noNamespace.SubscribeResponse;

import org.apache.xmlbeans.GDuration;

public abstract class ContextSubscriptionResponseChecker extends ResponseChecker {

	protected SubscribeResponse subscribeResponse;

	protected SubscribeError subscribeError;

	private GDuration expectedDuration = null;

	private GDuration expectedThrottling = null;

	private TopLevelElement expectedTopLevelElement;

	void setExpectedDuration(GDuration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	void setExpectedThrottling(GDuration expectedThrottling) {
		this.expectedThrottling = expectedThrottling;
	}

	void setExpectedTopLevelElement(TopLevelElement expectedTopLevelElement) {
		this.expectedTopLevelElement = expectedTopLevelElement;
	}

	@Override
	protected void assertContent() {
		extractSubscribeResponse();
		extractSubscribeError();
		assertSubscribeResponse();
		assertSubscribeError();
	}

	protected abstract void extractSubscribeResponse();

	protected abstract void extractSubscribeError();

	private void assertSubscribeResponse() {
		switch (expectedTopLevelElement) {
		case CONTENT:
			assertNotNull(subscribeResponse);
			assertSubscribeResponseContent();
			break;
		case ERROR:
			assertNull(subscribeResponse);
			break;
		default:
			throw new RuntimeException("expected top level element not set.");
		}
	}

	private void assertSubscribeResponseContent() {
		assertResponseSubscriptionID();
		assertDuration();
		assertThrottling();
	}

	private void assertResponseSubscriptionID() {
		String actualSubscriptionId = subscribeResponse.getSubscriptionId();
		assertNotNull(actualSubscriptionId);
		if (null == expectedID) {
			checkForExpectedIDIsUnset(actualSubscriptionId);
		} else {
			checkForExpectedIDIsSet(actualSubscriptionId);
		}
	}

	protected abstract void checkForExpectedIDIsUnset(String actualSubscriptionId);

	protected abstract void checkForExpectedIDIsSet(String actualSubscriptionId);

	private void assertDuration() {
		if (null != expectedDuration) {
			GDuration actualDuration = subscribeResponse.getDuration();
			assertNotNull(actualDuration);
			assertEquals(expectedDuration, actualDuration);
		}
	}

	private void assertThrottling() {
		if (null != expectedThrottling) {
			GDuration actualThrottling = subscribeResponse.getThrottling();
			assertNotNull(actualThrottling);
			assertEquals(expectedThrottling, actualThrottling);
		}
	}

	private void assertSubscribeError() {
		switch (expectedTopLevelElement) {
		case CONTENT:
			assertNull(subscribeError);
			break;
		case ERROR:
			assertNotNull(subscribeError);
			assertSubscribeErrorContent();
			break;
		default:
			throw new RuntimeException("expected top level element not set.");
		}
	}

	private void assertSubscribeErrorContent() {
		String actualSubscriptionId = subscribeError.getSubscriptionId();
		checkErrorSubscriptionId(actualSubscriptionId);
		StatusCode statusCode = subscribeError.getErrorCode();
		assertNotNull(statusCode);
		assertEquals(expectedNGSIStatusCode, statusCode.getCode());
		assertEquals(expectedNGSIReasonPhrase, statusCode.getReasonPhrase());
		String statusCodeDetails = statusCode.getDetails().toString();
		for (String expectedErrorMessage : expectedErrorMessages) {
			assertTrue(statusCodeDetails.contains(expectedErrorMessage));
		}
	}

	protected abstract void checkErrorSubscriptionId(String actualSubscriptionId);

}
