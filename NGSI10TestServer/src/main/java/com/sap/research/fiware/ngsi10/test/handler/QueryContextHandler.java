package com.sap.research.fiware.ngsi10.test.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextElementResponse;
import noNamespace.ContextElementResponseList;
import noNamespace.ContextMetadata;
import noNamespace.ContextMetadataList;
import noNamespace.EntityId;
import noNamespace.QueryContextResponse;
import noNamespace.QueryContextResponseDocument;
import noNamespace.StatusCode;

import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;

import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseTestHelper;

public abstract class QueryContextHandler {

	protected HttpHeaders headers;
	protected UriInfo info;
	protected Request requestStr;
	protected String payload;
	protected RequestTestHelper<? extends XmlObject> reqHlp;
	private ResponseTestHelper<QueryContextResponseDocument> resHlp;
	private QueryContextResponseDocument resDoc;
	private QueryContextResponse resp;
	protected List<EntityId> responseEntityIds;
	protected List<String> responseAttributeNames;
	protected String typeName;
	protected String attributeDomainName;

	protected QueryContextHandler(HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		this.headers = headers;
		this.info = info;
		this.requestStr = requestStr;
		this.payload = payload;
	}

	public final Response handle() {
		
		initializeRequestHelper();
		
		initializeResponse();
		
		// try to parse request payload
		reqHlp.checkRequest();
		
		performChecks();
		
		if (!reqHlp.hasErrors()) {
			checkForNotFoundError();
		}
		
		// handle request errors
		if (reqHlp.hasErrors()) {
			return resHlp.toRestResponse(reqHlp, resDoc);
		}
		
		buildResponseEntityIds();
		buildResponseAttributeNames();
		buildResponse();
		
		ArrayList<String> errors = new ArrayList<String>();
		resHlp.isValidResponse(resDoc, errors);
		
		return Response.ok(resDoc.toString()).build();
	}

	protected abstract void initializeRequestHelper();

	private void initializeResponse() {
		resHlp = new ResponseTestHelper<QueryContextResponseDocument>();
		resDoc = QueryContextResponseDocument.Factory.newInstance();
		resp = resDoc.addNewQueryContextResponse();
	}

	protected abstract void performChecks();

	protected abstract void checkForNotFoundError();

	protected abstract void buildResponseEntityIds();

	protected abstract void buildResponseAttributeNames();

	private void buildResponse() {
		ContextElementResponseList responseList = resp.addNewContextResponseList();
		for (EntityId responseEntityId : responseEntityIds) {
			ContextElementResponse contextElementResponse = responseList.addNewContextElementResponse();
			buildContextElementResponse(responseEntityId, contextElementResponse);
		}
	}

	protected abstract void buildContextElementResponse(EntityId responseEntityId, ContextElementResponse contextElementResponse);

	protected void addOkStatusCode(ContextElementResponse contextElementResponse) {
		String detail = reqHlp.getRequestInformation();
		StatusCode statusCode = NgsiStatusCodes.Ok.toStatusCode(detail);
		contextElementResponse.setStatusCode(statusCode);
	}

	protected void addFullContextElement(EntityId responseEntityId, ContextElementResponse contextElementResponse) {
		ContextElement elem = contextElementResponse.addNewContextElement();
		addEntityId(responseEntityId, elem);
		addAttributeDomainName(elem);
		addAttributeList(elem);
		addDomainMetadata(elem);
	}

	protected void addEntityId(EntityId responseEntityId, ContextElement elem) {
		EntityId id = elem.addNewEntityId();
		id.setId(responseEntityId.getId());
		if (null == typeName) {
			id.setType(responseEntityId.getType());
		} else {
			id.setType(typeName);
		}
		id.setIsPattern(responseEntityId.getIsPattern());
	}

	private void addAttributeDomainName(ContextElement elem) {
		if (null == attributeDomainName) {
			elem.setAttributeDomainName("enviromental parameters");
		} else {
			elem.setAttributeDomainName(attributeDomainName);
		}
	}

	private void addAttributeList(ContextElement elem) {
		ContextAttributeList contextAttributeList = elem.addNewContextAttributeList();
		if (responseAttributeNames.isEmpty()) {
			addAttribute("temperature", contextAttributeList);
		} else {
			for (String attributeName : responseAttributeNames) {
				addAttribute(attributeName, contextAttributeList);
			}
		}
	}

	private void addAttribute(String name, ContextAttributeList contextAttributeList) {
		ContextAttribute contextAttribute = contextAttributeList.addNewContextAttribute();
		contextAttribute.setName(name);
		contextAttribute.setType("http://purl.oclc.org/NET/ssnx/qu/dim#Temperature");
		ContextMetadataList contextAttributeMetadataList = contextAttribute.addNewMetadata();
		ContextMetadata contextAttributeMetadata = contextAttributeMetadataList.addNewContextMetadata();
		contextAttributeMetadata.setName("Timestamp");
		contextAttributeMetadata.setType("xsd:dateTime");
		XmlDateTime dateTime = XmlDateTime.Factory.newInstance();
		dateTime.setDateValue(new Date(1000000000000l));
		contextAttributeMetadata.setValue(dateTime);
		contextAttribute.setContextValue(XmlString.Factory.newValue("23.42°C"));
	}

	private void addDomainMetadata(ContextElement elem) {
		ContextMetadataList domainMetadataList = elem.addNewDomainMetadata();
		ContextMetadata domainMetadata = domainMetadataList.addNewContextMetadata();
		domainMetadata.setName("Owner");
		domainMetadata.setType("xsd:string");
		domainMetadata.setValue(XmlString.Factory.newValue("SAP"));
	}

}
