package com.sap.research.fiware.ngsi10.test.responsecheckers;

import java.util.ArrayList;
import java.util.List;

public class FullContextElementCheckerBuilder extends ContextElementCheckerBuilder {

	private String expectedTypeName = null;

	private String expectedAttributeDomainName = null;

	private List<String> expectedAttributeNames = new ArrayList<String>();

	public static FullContextElementCheckerBuilder create() {
		return new FullContextElementCheckerBuilder();
	}

	public FullContextElementCheckerBuilder expectedId(String expectedId) {
		setExpectedId(expectedId);
		return this;
	}

	public FullContextElementCheckerBuilder expectedTypeName(String expectedTypeName) {
		this.expectedTypeName = expectedTypeName;
		return this;
	}

	public FullContextElementCheckerBuilder expectedAttributeDomainName(String expectedAttributeDomainName) {
		this.expectedAttributeDomainName = expectedAttributeDomainName;
		return this;
	}

	public FullContextElementCheckerBuilder expectedAttributeName(String expectedAttributeName) {
		if (null == expectedAttributeName) {
			throw new RuntimeException("expectedAttributeName must not be null");
		}
		expectedAttributeNames.add(expectedAttributeName);
		return this;
	}

	public FullContextElementChecker build() {
		if (null == expectedTypeName) {
			throw new RuntimeException("expectedTypeName must not be null");
		}
		if (null == expectedAttributeDomainName) {
			throw new RuntimeException("expectedAttributeDomainName must not be null");
		}
		if (0 == expectedAttributeNames.size()) {
			throw new RuntimeException("expectedAttributeNames must not be empty");
		}
		FullContextElementChecker fullContextElementChecker = new FullContextElementChecker();
		super.build(fullContextElementChecker);
		fullContextElementChecker.setExpectedTypeName(expectedTypeName);
		fullContextElementChecker.setExpectedAttributeDomainName(expectedAttributeDomainName);
		fullContextElementChecker.setExpectedAttributeNames(expectedAttributeNames);
		return fullContextElementChecker;
	}

}
