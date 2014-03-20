package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextMetadataList;
import noNamespace.EntityId;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;

public class SimpleContextElementChecker extends ContextElementChecker {

	private String expectedAttributeName = null;

	private String expectedAttributeValue = null;

	SimpleContextElementChecker() {
	}

	void setExpectedAttributeName(String expectedAttributeName) {
		this.expectedAttributeName = expectedAttributeName;
	}

	void setExpectedAttributeValue(String expectedAttributeValue) {
		this.expectedAttributeValue = expectedAttributeValue;
	}

	@Override
	public void assertContextElement(ContextElement contextElement) {
		assertNotNull(contextElement);
		EntityId entityId = contextElement.getEntityId();
		assertNotNull(entityId);
		String id = entityId.getId();
		assertEquals(expectedId, id);
		String entityIDType = entityId.getType();
		assertNull(entityIDType);
		assertEquals(false, entityId.getIsPattern());
		String attributeDomainName = contextElement.getAttributeDomainName();
		assertNull(attributeDomainName);
		ContextAttributeList contextAttributeList = contextElement.getContextAttributeList();
		assertNotNull(contextAttributeList);
		ContextAttribute[] contextAttributes = contextAttributeList.getContextAttributeArray();
		assertEquals(1, contextAttributes.length);
		ContextAttribute firstContextAttribute = contextAttributes[0];
		assertNotNull(firstContextAttribute);
		String attributeName = firstContextAttribute.getName();
		assertEquals(expectedAttributeName, attributeName);
		String attributeType = firstContextAttribute.getType();
		assertNull(attributeType);
		XmlObject attributeValue = firstContextAttribute.getContextValue();
		assertTrue(attributeValue instanceof XmlAnyTypeImpl);
		assertEquals(expectedAttributeValue, ((XmlAnyTypeImpl) attributeValue).getStringValue());
		ContextMetadataList attributeMetadataList = firstContextAttribute.getMetadata();
		assertNull(attributeMetadataList);
		ContextMetadataList domainMetadataList = contextElement.getDomainMetadata();
		assertNull(domainMetadataList);
	}

}
