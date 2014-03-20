package com.sap.research.fiware.ngsi10.test.responsecheckers;

public class UnsubscribeContextResponseCheckerBuilder extends ResponseCheckerBuilder {

	private String expectedID = null;

	public static UnsubscribeContextResponseCheckerBuilder create() {
		return new UnsubscribeContextResponseCheckerBuilder();
	}

	public UnsubscribeContextResponseCheckerBuilder expectedHTTPStatusCode(int expectedHTTPStatusCode) {
		setExpectedHTTPStatusCode(expectedHTTPStatusCode);
		return this;
	}

	public UnsubscribeContextResponseCheckerBuilder expectedID(String expectedID) {
		this.expectedID = expectedID;
		return this;
	}

	public UnsubscribeContextResponseCheckerBuilder expectedNGSIStatusCode(int expectedNGSIStatusCode, String expectedNGSIReasonPhrase) {
		setExpectedNGSIStatusCode(expectedNGSIStatusCode, expectedNGSIReasonPhrase);
		return this;
	}

	public UnsubscribeContextResponseCheckerBuilder expectedMessages(String... expectedMessages) {
		setExpectedMessages(expectedMessages);
		return this;
	}

	public UnsubscribeContextResponseCheckerBuilder expectedErrorMessages(String... expectedErrorMessages) {
		setExpectedErrorMessages(expectedErrorMessages);
		return this;
	}

	public UnsubscribeContextResponseChecker build() {
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = new UnsubscribeContextResponseChecker();
		super.build(unsubscribeContextResponseChecker);
		unsubscribeContextResponseChecker.setExpectedID(expectedID);
		return unsubscribeContextResponseChecker;
	}

}
