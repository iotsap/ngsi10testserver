package com.sap.research.fiware.ngsi10.test.responsecheckers;

import noNamespace.ContextElement;

public abstract class ContextElementChecker {

	protected String expectedId = null;

	protected void setExpectedId(String expectedId) {
		this.expectedId = expectedId;
	}

	public String getExpectedId() {
		return expectedId;
	}

	public abstract void assertContextElement(ContextElement contextElement);

}
