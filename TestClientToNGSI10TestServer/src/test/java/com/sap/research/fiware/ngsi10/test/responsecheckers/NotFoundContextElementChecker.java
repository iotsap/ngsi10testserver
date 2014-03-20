package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;
import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.EntityId;

public class NotFoundContextElementChecker extends ContextElementChecker {

	private String expectedTypeName = null;
	private String expectedAttribute = null;
	
	NotFoundContextElementChecker() {
	}

	void setExpectedTypeName(String expectedTypeName) {
		this.expectedTypeName = expectedTypeName;
	}

	void setExpectedAttribute(String expectedAttribute) {
		this.expectedAttribute = expectedAttribute;
	}

	@Override
	public void assertContextElement(ContextElement contextElement) {
		assertNotNull(contextElement);
		EntityId entityId = contextElement.getEntityId();
		assertNotNull(entityId);
		String id = entityId.getId();
		assertEquals(expectedId, id);
		String entityIDType = entityId.getType();
		assertNotNull(entityIDType);
		assertEquals(expectedTypeName, entityIDType);
		assertEquals(false, entityId.getIsPattern());
		assertNull(contextElement.getAttributeDomainName());
		ContextAttributeList contextAttributeList = contextElement.getContextAttributeList();
		if (null == expectedAttribute) {
			assertNull(contextAttributeList);
		} else {
			assertNotNull(contextAttributeList);
			assertExpectedAttribute(contextAttributeList);
		}
		assertNull(contextElement.getDomainMetadata());
	}

	private void assertExpectedAttribute(ContextAttributeList contextAttributeList) {
		ContextAttribute[] contextAttributes = contextAttributeList.getContextAttributeArray();
		assertEquals(1, contextAttributes.length);
		ContextAttribute contextAttribute = contextAttributes[0];
		assertNotNull(contextAttribute);
		assertEquals(expectedAttribute, contextAttribute.getName());
		assertNull(contextAttribute.getContextValue());
		assertNull(contextAttribute.getMetadata());
		assertNull(contextAttribute.getType());
	}

}
