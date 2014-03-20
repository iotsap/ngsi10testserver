package com.sap.research.fiware.ngsi10.test.responsecheckers;

import org.apache.xmlbeans.GDuration;

public class UpdateContextSubscriptionResponseCheckerBuilder extends ContextSubscriptionResponseCheckerBuilder {

	private String expectedID = null;

	public static UpdateContextSubscriptionResponseCheckerBuilder create() {
		return new UpdateContextSubscriptionResponseCheckerBuilder();
	}

	public UpdateContextSubscriptionResponseCheckerBuilder expectedHTTPStatusCode(int expectedHTTPStatusCode) {
		setExpectedHTTPStatusCode(expectedHTTPStatusCode);
		return this;
	}

	public UpdateContextSubscriptionResponseCheckerBuilder expectedID(String expectedID) {
		this.expectedID = expectedID;
		return this;
	}

	public UpdateContextSubscriptionResponseCheckerBuilder expectedDuration(GDuration expectedDuration) {
		setExpectedDuration(expectedDuration);
		return this;
	}

	public UpdateContextSubscriptionResponseCheckerBuilder expectedThrottling(GDuration expectedThrottling) {
		setExpectedThrottling(expectedThrottling);
		return this;
	}

	public UpdateContextSubscriptionResponseCheckerBuilder expectedTopLevelElement(TopLevelElement expectedTopLevelElement) {
		setExpectedTopLevelElement(expectedTopLevelElement);
		return this;
	}

	public UpdateContextSubscriptionResponseCheckerBuilder expectedNGSIStatusCode(int expectedNGSIStatusCode, String expectedNGSIReasonPhrase) {
		setExpectedNGSIStatusCode(expectedNGSIStatusCode, expectedNGSIReasonPhrase);
		return this;
	}

	public UpdateContextSubscriptionResponseCheckerBuilder expectedErrorMessages(String... expectedErrorMessages) {
		setExpectedErrorMessages(expectedErrorMessages);
		return this;
	}

	public UpdateContextSubscriptionResponseChecker build() {
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = new UpdateContextSubscriptionResponseChecker();
		super.build(updateContextSubscriptionResponseChecker);
		updateContextSubscriptionResponseChecker.setExpectedID(expectedID);
		return updateContextSubscriptionResponseChecker;
	}

}
