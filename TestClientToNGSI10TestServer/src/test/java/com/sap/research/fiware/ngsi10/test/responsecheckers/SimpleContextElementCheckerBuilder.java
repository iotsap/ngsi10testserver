package com.sap.research.fiware.ngsi10.test.responsecheckers;

public class SimpleContextElementCheckerBuilder extends ContextElementCheckerBuilder {

	private String expectedAttributeName = null;

	private String expectedAttributeValue = null;

	public static SimpleContextElementCheckerBuilder create() {
		return new SimpleContextElementCheckerBuilder();
	}

	public SimpleContextElementCheckerBuilder expectedId(String expectedId) {
		setExpectedId(expectedId);
		return this;
	}

	public SimpleContextElementCheckerBuilder expectedAttributeName(String expectedAttributeName) {
		this.expectedAttributeName = expectedAttributeName;
		return this;
	}

	public SimpleContextElementCheckerBuilder expectedAttributeValue(String expectedAttributeValue) {
		this.expectedAttributeValue = expectedAttributeValue;
		return this;
	}

	public SimpleContextElementChecker build() {
		if (null == expectedAttributeName) {
			throw new RuntimeException("expectedAttributeName must not be null");
		}
		if (null == expectedAttributeValue) {
			throw new RuntimeException("expectedAttributeValue must not be null");
		}
		SimpleContextElementChecker simpleContextElementChecker = new SimpleContextElementChecker();
		super.build(simpleContextElementChecker);
		simpleContextElementChecker.setExpectedAttributeName(expectedAttributeName);
		simpleContextElementChecker.setExpectedAttributeValue(expectedAttributeValue);
		return simpleContextElementChecker;
	}

}
