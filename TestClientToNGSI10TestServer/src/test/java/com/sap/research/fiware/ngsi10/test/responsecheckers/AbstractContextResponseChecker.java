package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import noNamespace.ContextElement;
import noNamespace.ContextElementResponse;
import noNamespace.ContextElementResponseList;
import noNamespace.EntityId;
import noNamespace.StatusCode;

public abstract class AbstractContextResponseChecker extends ResponseChecker {

	private Map<String, ContextElementChecker> contextElementCheckers = new HashMap<String, ContextElementChecker>();

	private TopLevelElement expectedTopLevelElement;

	protected StatusCode errorCode;

	protected ContextElementResponseList contextResponseList;

	void setContextElementCheckers(Map<String, ContextElementChecker> contextElementCheckers) {
		this.contextElementCheckers = contextElementCheckers;
	}

	void setExpectedTopLevelElement(TopLevelElement expectedTopLevelElement) {
		this.expectedTopLevelElement = expectedTopLevelElement;
	}

	@Override
	protected void assertContent() {
		extractStatusCodeAndResponseList();
		switch (expectedTopLevelElement) {
		case CONTENT:
			assertNull(errorCode);
			assertNotNull(contextResponseList);
			assertContent(contextResponseList);
			break;
		case ERROR:
			assertNotNull(errorCode);
			assertContent(errorCode);
			assertNull(contextResponseList);
			break;
		default:
			throw new RuntimeException("expected top level element not set.");
		}
	}

	protected abstract void extractStatusCodeAndResponseList();

	private void assertContent(ContextElementResponseList contextElementResponseList) {
		ContextElementResponse[] contextElementResponseArray = contextElementResponseList.getContextElementResponseArray();
		assertEquals(contextElementCheckers.size(), contextElementResponseArray.length);
		for (int i = 0; i < contextElementResponseArray.length; i++) {
			ContextElementResponse contextElementResponse = contextElementResponseArray[i];
			
			ContextElement contextElement = contextElementResponse.getContextElement();
			assertNotNull(contextElement);
			EntityId entityId = contextElement.getEntityId();
			assertNotNull(entityId);
			String id = entityId.getId();
			assertNotNull(id);
			ContextElementChecker contextElementChecker = contextElementCheckers.get(id);
			assertNotNull("Unexpected Id " + id, contextElementChecker);
			contextElementChecker.assertContextElement(contextElement);
			
			StatusCode statusCode = contextElementResponse.getStatusCode();
			assertNotNull(statusCode);
			assertEquals(expectedNGSIStatusCode, statusCode.getCode());
			assertEquals(expectedNGSIReasonPhrase, statusCode.getReasonPhrase());
			String statusCodeDetails = statusCode.getDetails().toString();
			if (null != expectedMessages) {
				for (String expectedMessage : expectedMessages) {
					assertTrue(statusCodeDetails.contains(expectedMessage));
				}
			}
		}
	}

	private void assertContent(StatusCode statusCode) {
		assertEquals(expectedNGSIStatusCode, statusCode.getCode());
		assertEquals(expectedNGSIReasonPhrase, statusCode.getReasonPhrase());
		String statusCodeDetails = statusCode.getDetails().toString();
		if (null != expectedErrorMessages) {
			for (String expectedErrorMessage : expectedErrorMessages) {
				assertTrue(statusCodeDetails.contains(expectedErrorMessage));
			}
		}
	}

}