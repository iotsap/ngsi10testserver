package com.sap.research.fiware.ngsi10.test.responsecheckers;

public class UpdateContextResponseCheckerBuilder extends AbstractContextResponseCheckerBuilder {

	public static UpdateContextResponseCheckerBuilder create() {
		return new UpdateContextResponseCheckerBuilder();
	}

	public UpdateContextResponseCheckerBuilder expectedHTTPStatusCode(int expectedHTTPStatusCode) {
		setExpectedHTTPStatusCode(expectedHTTPStatusCode);
		return this;
	}

	public UpdateContextResponseCheckerBuilder contextElementChecker(ContextElementChecker contextElementChecker) {
		addContextElementChecker(contextElementChecker);
		return this;
	}

	public UpdateContextResponseCheckerBuilder expectedTopLevelElement(TopLevelElement expectedTopLevelElement) {
		setExpectedTopLevelElement(expectedTopLevelElement);
		return this;
	}

	public UpdateContextResponseCheckerBuilder expectedNGSIStatusCode(int expectedNGSIStatusCode, String expectedNGSIReasonPhrase) {
		setExpectedNGSIStatusCode(expectedNGSIStatusCode, expectedNGSIReasonPhrase);
		return this;
	}

	public UpdateContextResponseCheckerBuilder expectedMessages(String... expectedMessages) {
		setExpectedMessages(expectedMessages);
		return this;
	}

	public UpdateContextResponseCheckerBuilder expectedErrorMessages(String... expectedErrorMessages) {
		setExpectedErrorMessages(expectedErrorMessages);
		return this;
	}

	public UpdateContextResponseChecker build() {
		UpdateContextResponseChecker updateContextResponseChecker = new UpdateContextResponseChecker();
		super.build(updateContextResponseChecker);
		return updateContextResponseChecker;
	}

}
