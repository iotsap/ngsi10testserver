package com.sap.research.fiware.ngsi10.test.responsecheckers;

public abstract class ContextElementCheckerBuilder {

	private String expectedId = null;

	protected void setExpectedId(String expectedId) {
		this.expectedId = expectedId;
	}

	protected void build(ContextElementChecker checker) {
		if (null == expectedId) {
			throw new RuntimeException("expectedId must not be null");
		}
		checker.setExpectedId(expectedId);
	}

}
