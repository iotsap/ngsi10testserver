package com.sap.research.fiware.ngsi10.test.responsecheckers;

import org.apache.xmlbeans.GDuration;

public class SubscribeContextResponseCheckerBuilder extends ContextSubscriptionResponseCheckerBuilder {

	public static SubscribeContextResponseCheckerBuilder create() {
		return new SubscribeContextResponseCheckerBuilder();
	}

	public SubscribeContextResponseCheckerBuilder expectedHTTPStatusCode(int expectedHTTPStatusCode) {
		setExpectedHTTPStatusCode(expectedHTTPStatusCode);
		return this;
	}

	public SubscribeContextResponseCheckerBuilder expectedDuration(GDuration expectedDuration) {
		setExpectedDuration(expectedDuration);
		return this;
	}

	public SubscribeContextResponseCheckerBuilder expectedThrottling(GDuration expectedThrottling) {
		setExpectedThrottling(expectedThrottling);
		return this;
	}

	public SubscribeContextResponseCheckerBuilder expectedTopLevelElement(TopLevelElement expectedTopLevelElement) {
		setExpectedTopLevelElement(expectedTopLevelElement);
		return this;
	}

	public SubscribeContextResponseCheckerBuilder expectedNGSIStatusCode(int expectedNGSIStatusCode, String expectedNGSIReasonPhrase) {
		setExpectedNGSIStatusCode(expectedNGSIStatusCode, expectedNGSIReasonPhrase);
		return this;
	}

	public SubscribeContextResponseCheckerBuilder expectedErrorMessages(String... expectedErrorMessages) {
		setExpectedErrorMessages(expectedErrorMessages);
		return this;
	}

	public SubscribeContextResponseChecker build() {
		SubscribeContextResponseChecker subscribeContextResponseChecker = new SubscribeContextResponseChecker();
		super.build(subscribeContextResponseChecker);
		return subscribeContextResponseChecker;
	}

}
