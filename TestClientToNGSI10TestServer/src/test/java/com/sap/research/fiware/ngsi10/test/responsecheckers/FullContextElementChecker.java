package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextMetadata;
import noNamespace.ContextMetadataList;
import noNamespace.EntityId;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.apache.xmlbeans.impl.values.XmlDateTimeImpl;

public class FullContextElementChecker extends ContextElementChecker {

	private String expectedTypeName = null;

	private String expectedAttributeDomainName = null;

	private List<String> expectedAttributeNames = new ArrayList<String>();

	FullContextElementChecker() {
	}

	void setExpectedTypeName(String expectedTypeName) {
		this.expectedTypeName = expectedTypeName;
	}

	void setExpectedAttributeDomainName(String expectedAttributeDomainName) {
		this.expectedAttributeDomainName = expectedAttributeDomainName;
	}

	void setExpectedAttributeNames(List<String> expectedAttributeNames) {
		this.expectedAttributeNames = expectedAttributeNames;
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
		String attributeDomainName = contextElement.getAttributeDomainName();
		assertEquals(expectedAttributeDomainName, attributeDomainName);
		ContextAttributeList contextAttributeList = contextElement.getContextAttributeList();
		assertNotNull(contextAttributeList);
		ContextAttribute[] contextAttributes = contextAttributeList.getContextAttributeArray();
		assertEquals(expectedAttributeNames.size(), contextAttributes.length);
		for (int i = 0; i < contextAttributes.length; i++) {
			ContextAttribute contextAttribute = contextAttributes[i];
			assertNotNull(contextAttribute);
			String attributeName = contextAttribute.getName();
			assertTrue(expectedAttributeNames.contains(attributeName));
			String attributeType = contextAttribute.getType();
			assertEquals("http://purl.oclc.org/NET/ssnx/qu/dim#Temperature", attributeType);
			XmlObject attributeValue = contextAttribute.getContextValue();
			assertTrue(attributeValue instanceof XmlAnyTypeImpl);
			assertEquals("23.42°C", ((XmlAnyTypeImpl) attributeValue).getStringValue());
			ContextMetadataList attributeMetadataList = contextAttribute.getMetadata();
			assertNotNull(attributeMetadataList);
			ContextMetadata[] attributeMetadata = attributeMetadataList.getContextMetadataArray();
			assertEquals(1, attributeMetadata.length);
			ContextMetadata firstAttributeMetadata = attributeMetadata[0];
			String attributeMetadataName = firstAttributeMetadata.getName();
			assertEquals("Timestamp", attributeMetadataName);
			String attributeMetadataType = firstAttributeMetadata.getType();
			assertEquals("xsd:dateTime", attributeMetadataType);
			XmlObject attributeMetadataValue = firstAttributeMetadata.getValue();
			assertTrue(attributeMetadataValue instanceof XmlDateTimeImpl);
			assertEquals(new Date(1000000000000l), ((XmlDateTimeImpl) attributeMetadataValue).getDateValue());
		}
		ContextMetadataList domainMetadataList = contextElement.getDomainMetadata();
		assertNotNull(domainMetadataList);
		ContextMetadata[] domainMetadata = domainMetadataList.getContextMetadataArray();
		assertEquals(1, domainMetadata.length);
		ContextMetadata firstDomainMetadata = domainMetadata[0];
		String domainMetadataName = firstDomainMetadata.getName();
		assertEquals("Owner", domainMetadataName);
		String domainMetadataType = firstDomainMetadata.getType();
		assertEquals("xsd:string", domainMetadataType);
		XmlObject domainMetadataValue = firstDomainMetadata.getValue();
		assertTrue(domainMetadataValue instanceof XmlAnyTypeImpl);
		assertEquals("SAP", ((XmlAnyTypeImpl) domainMetadataValue).getStringValue());
	}

}
