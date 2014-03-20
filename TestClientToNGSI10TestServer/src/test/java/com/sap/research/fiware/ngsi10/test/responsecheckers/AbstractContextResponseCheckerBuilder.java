package com.sap.research.fiware.ngsi10.test.responsecheckers;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractContextResponseCheckerBuilder extends ResponseCheckerBuilder {

	private Map<String, ContextElementChecker> contextElementCheckers = new HashMap<String, ContextElementChecker>();

	private TopLevelElement expectedTopLevelElement;

	protected void addContextElementChecker(ContextElementChecker contextElementChecker) {
		String expectedId = contextElementChecker.getExpectedId();
		if (null == expectedId) {
			throw new RuntimeException("Can't check for a ContextElement without ID");
		}
		contextElementCheckers.put(expectedId, contextElementChecker);
	}

	protected void setExpectedTopLevelElement(TopLevelElement exptectedTopLevelElement) {
		this.expectedTopLevelElement = exptectedTopLevelElement;
	}

	protected void build(AbstractContextResponseChecker checker) {
		super.build(checker);
		checker.setContextElementCheckers(contextElementCheckers);
		checker.setExpectedTopLevelElement(expectedTopLevelElement);
	}
}
