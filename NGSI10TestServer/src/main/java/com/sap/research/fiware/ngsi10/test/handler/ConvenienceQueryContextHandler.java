package com.sap.research.fiware.ngsi10.test.handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import noNamespace.ContextElementResponse;
import noNamespace.EntityId;

import org.apache.xmlbeans.XmlObject;

import com.sap.research.fiware.ngsi10.test.helpers.Constants;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;

public class ConvenienceQueryContextHandler extends QueryContextHandler {

	private String attributeName;

	public ConvenienceQueryContextHandler(String typeName, String attributeName, String attributeDomainName, HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		super(headers, info, requestStr, payload);
		this.typeName = typeName;
		this.attributeName = attributeName;
		this.attributeDomainName = attributeDomainName;
	}

	@Override
	protected void initializeRequestHelper() {
		reqHlp = new RequestTestHelper<XmlObject>(requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(null);
		reqHlp.setAllowsQueryParams(true);
		reqHlp.setRequiresPayload(false);
	}

	@Override
	protected void performChecks() {
		// check if typeName is a valid URI
		try {
			new URI(typeName);
		} catch (URISyntaxException e) {
			String description = "typeName '" + typeName + "' is not a valid URI: " + e.getMessage();
			reqHlp.addNewIssueRecord(Status.BAD_REQUEST, description, NgsiStatusCodes.BadRequest);
		}
	}

	@Override
	protected void checkForNotFoundError() {
		if (null != typeName) {
			if (typeName.contains(Constants.NOT_FOUND)) {
				String details = "Context entity type with type name \"" + typeName
						+ "\" not found. *You* have provoked this error by"
						+ " specifying a type name containing the string \""
						+ Constants.NOT_FOUND + "\" in the request. (here: \""
						+ typeName + "\")";
				reqHlp.addNewIssueRecord(Status.NOT_FOUND, details, NgsiStatusCodes.ContextElementNotFound);
			}
		}
		if (null != attributeName) {
			if (attributeName.contains(Constants.NOT_FOUND)) {
				String details = "Attribute with name \"" + attributeName
						+ "\" not found. *You* have provoked this error by"
						+ " specifying an attribute name containing the string \""
						+ Constants.NOT_FOUND + "\" in the request. (here: \""
						+ attributeName + "\")";
				reqHlp.addNewIssueRecord(Status.NOT_FOUND, details, NgsiStatusCodes.ContextElementNotFound);
			}
		}
		if (null != attributeDomainName) {
			if (attributeDomainName.contains(Constants.NOT_FOUND)) {
				String details = "Attribute domain with name \"" + attributeDomainName
						+ "\" not found. *You* have provoked this error by"
						+ " specifying an attribute domain name containing the string \""
						+ Constants.NOT_FOUND + "\" in the request. (here: \""
						+ attributeDomainName + "\")";
				reqHlp.addNewIssueRecord(Status.NOT_FOUND, details, NgsiStatusCodes.ContextElementNotFound);
			}
		}
	}

	@Override
	protected void buildResponseEntityIds() {
		responseEntityIds = new ArrayList<EntityId>();
		EntityId entityId = EntityId.Factory.newInstance();
		entityId.setId("Crate1");
		entityId.setType(typeName);
		entityId.setIsPattern(false);
		responseEntityIds.add(entityId);
	}

	@Override
	protected void buildResponseAttributeNames() {
		responseAttributeNames = new ArrayList<String>();
		if (null == attributeName) {
			responseAttributeNames.add("temperature");
		} else {
			responseAttributeNames.add(attributeName);
		}
	}

	@Override
	protected void buildContextElementResponse(EntityId responseEntityId, ContextElementResponse contextElementResponse) {
		addOkStatusCode(contextElementResponse);
		addFullContextElement(responseEntityId, contextElementResponse);
	}

}
