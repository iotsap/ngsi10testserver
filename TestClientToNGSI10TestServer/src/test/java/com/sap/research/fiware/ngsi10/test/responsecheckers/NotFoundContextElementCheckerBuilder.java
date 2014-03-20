package com.sap.research.fiware.ngsi10.test.responsecheckers;

public class NotFoundContextElementCheckerBuilder extends ContextElementCheckerBuilder {

	private String expectedTypeName = null;
	private String expectedAttribute = null;

	public static NotFoundContextElementCheckerBuilder create() {
		return new NotFoundContextElementCheckerBuilder();
	}

	public NotFoundContextElementCheckerBuilder expectedId(String expectedId) {
		setExpectedId(expectedId);
		return this;
	}

	public NotFoundContextElementCheckerBuilder expectedTypeName(String expectedTypeName) {
		this.expectedTypeName = expectedTypeName;
		return this;
	}

	public NotFoundContextElementCheckerBuilder expectedAttribute(String expectedAttribute) {
		this.expectedAttribute = expectedAttribute;
		return this;
	}

	public NotFoundContextElementChecker build() {
		if (null == expectedTypeName) {
			throw new RuntimeException("expectedTypeName must not be null");
		}
		NotFoundContextElementChecker notFoundContextElementChecker = new NotFoundContextElementChecker();
		super.build(notFoundContextElementChecker);
		notFoundContextElementChecker.setExpectedTypeName(expectedTypeName);
		notFoundContextElementChecker.setExpectedAttribute(expectedAttribute);
		return notFoundContextElementChecker;
	}

}
