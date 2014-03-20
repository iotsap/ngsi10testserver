package com.sap.research.fiware.ngsi10.test.responsecheckers;

import java.util.ArrayList;
import java.util.List;

public abstract class ResponseCheckerBuilder {

	private int expectedHTTPStatusCode = -1;
	private int expectedNGSIStatusCode = -1;
	private String expectedNGSIReasonPhrase = null;
	private List<String> expectedErrorMessages = null;
	private List<String> expectedMessages = null;

	protected void setExpectedHTTPStatusCode(int expectedHTTPStatusCode) {
		this.expectedHTTPStatusCode = expectedHTTPStatusCode;
	}

	protected void setExpectedNGSIStatusCode(int expectedNGSIStatusCode, String expectedNGSIReasonPhrase) {
		this.expectedNGSIStatusCode = expectedNGSIStatusCode;
		this.expectedNGSIReasonPhrase = expectedNGSIReasonPhrase;
	}

	protected void setExpectedErrorMessages(String... expectedErrorMessages) {
		this.expectedErrorMessages = new ArrayList<String>();
		for (String expectedErrorMessage : expectedErrorMessages) {
			this.expectedErrorMessages.add(expectedErrorMessage);
		}
	}

	protected void setExpectedMessages(String... expectedMessages) {
		this.expectedMessages = new ArrayList<String>();
		for (String expectedMessage : expectedMessages) {
			this.expectedMessages.add(expectedMessage);
		}
	}

	protected void build(ResponseChecker checker) {
		checker.setExpectedHTTPStatusCode(expectedHTTPStatusCode);
		checker.setExpectedNGSIStatusCode(expectedNGSIStatusCode, expectedNGSIReasonPhrase);
		checker.setExpectedErrorMessages(expectedErrorMessages);
		checker.setExpectedMessages(expectedMessages);
	}

}
