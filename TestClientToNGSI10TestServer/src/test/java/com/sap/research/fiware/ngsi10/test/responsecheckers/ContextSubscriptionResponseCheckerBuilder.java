package com.sap.research.fiware.ngsi10.test.responsecheckers;

import org.apache.xmlbeans.GDuration;

public abstract class ContextSubscriptionResponseCheckerBuilder extends ResponseCheckerBuilder {

	private GDuration expectedDuration = null;
	private GDuration expectedThrottling = null;
	private TopLevelElement expectedTopLevelElement;

	protected void setExpectedDuration(GDuration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	protected void setExpectedThrottling(GDuration expectedThrottling) {
		this.expectedThrottling = expectedThrottling;
	}

	protected void setExpectedTopLevelElement(TopLevelElement expectedTopLevelElement) {
		this.expectedTopLevelElement = expectedTopLevelElement;
	}

	protected void build(ContextSubscriptionResponseChecker checker) {
		super.build(checker);
		checker.setExpectedDuration(expectedDuration);
		checker.setExpectedThrottling(expectedThrottling);
		checker.setExpectedTopLevelElement(expectedTopLevelElement);
	}

}
