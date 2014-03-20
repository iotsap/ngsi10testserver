package com.sap.research.fiware.ngsi10.test.responsecheckers;

public class QueryContextResponseCheckerBuilder extends AbstractContextResponseCheckerBuilder {

	public static QueryContextResponseCheckerBuilder create() {
		return new QueryContextResponseCheckerBuilder();
	}

	public QueryContextResponseCheckerBuilder expectedHTTPStatusCode(int expectedHTTPStatusCode) {
		setExpectedHTTPStatusCode(expectedHTTPStatusCode);
		return this;
	}

	public QueryContextResponseCheckerBuilder contextElementChecker(ContextElementChecker contextElementChecker) {
		addContextElementChecker(contextElementChecker);
		return this;
	}

	public QueryContextResponseCheckerBuilder expectedTopLevelElement(TopLevelElement expectedTopLevelElement) {
		setExpectedTopLevelElement(expectedTopLevelElement);
		return this;
	}

	public QueryContextResponseCheckerBuilder expectedNGSIStatusCode(int expectedNGSIStatusCode, String expectedNGSIReasonPhrase) {
		setExpectedNGSIStatusCode(expectedNGSIStatusCode, expectedNGSIReasonPhrase);
		return this;
	}

	public QueryContextResponseCheckerBuilder expectedMessages(String... expectedMessages) {
		setExpectedMessages(expectedMessages);
		return this;
	}

	public QueryContextResponseCheckerBuilder expectedErrorMessages(String... expectedErrorMessages) {
		setExpectedErrorMessages(expectedErrorMessages);
		return this;
	}

	public QueryContextResponseChecker build() {
		QueryContextResponseChecker queryContextResponseChecker = new QueryContextResponseChecker();
		super.build(queryContextResponseChecker);
		return queryContextResponseChecker;
	}

}
