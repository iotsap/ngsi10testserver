package com.sap.research.fiware.ngsi10.test.handler;

import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import noNamespace.AttributeList;
import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextElementResponse;
import noNamespace.EntityId;
import noNamespace.EntityIdList;
import noNamespace.QueryContextRequest;
import noNamespace.QueryContextRequestDocument;
import noNamespace.StatusCode;

import com.sap.research.fiware.ngsi10.test.helpers.Constants;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;

public class StandardQueryContextHandler extends QueryContextHandler {

	private QueryContextRequest queryContextRequest;
	private EntityId[] requestEntityIds;

	public StandardQueryContextHandler(HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		super(headers, info, requestStr, payload);
	}

	@Override
	protected void initializeRequestHelper() {
		reqHlp = new RequestTestHelper<QueryContextRequestDocument>(requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(true);
	}

	@Override
	protected void performChecks() {
		// check EntityIds in request
		QueryContextRequestDocument requestDoc = (QueryContextRequestDocument) reqHlp.getReqDoc();
		if (null == requestDoc) {
			String description = "Request document must not be null";
			reqHlp.addNewIssueRecord(Status.BAD_REQUEST, description,
					NgsiStatusCodes.BadRequest);
			return;
		}
		queryContextRequest = requestDoc.getQueryContextRequest();
		EntityIdList requestEntityIdList = queryContextRequest.getEntityIdList();
		if (null == requestEntityIdList) {
			String description = "EntityIdList must not be null";
			reqHlp.addNewIssueRecord(Status.BAD_REQUEST, description,
					NgsiStatusCodes.BadRequest);
			return;
		}
		requestEntityIds = requestEntityIdList.getEntityIdArray();
		if (0 == requestEntityIds.length) {
			String description = "EntityIdList must not be empty";
			reqHlp.addNewIssueRecord(Status.BAD_REQUEST, description,
					NgsiStatusCodes.BadRequest);
		}
	}

	@Override
	protected void checkForNotFoundError() {
		// In the standard case this is handled for every EntityId individually.
		// See method notFound(EntityId responseEntityId).
	}

	@Override
	protected void buildResponseEntityIds() {
		responseEntityIds = new ArrayList<EntityId>();
		for (int i = 0; i < requestEntityIds.length; i++) {
			EntityId entityId = requestEntityIds[i];
			EntityId responseEntityId = EntityId.Factory.newInstance();
			responseEntityId.setId(entityId.getId());
			if (null == entityId.getType()) {
				responseEntityId.setType("http://frc.research.sap.com/semantic#Crate");
			} else {
				responseEntityId.setType(entityId.getType());
			}
			responseEntityId.setIsPattern(entityId.getIsPattern());
			responseEntityIds.add(responseEntityId);
		}
	}

	@Override
	protected void buildResponseAttributeNames() {
		AttributeList attributeList = queryContextRequest.getAttributeList();
		if (null != attributeList) {
			String[] attributeStrings = attributeList.getAttributeArray();
			if (null != attributeStrings && 0 != attributeStrings.length) {
				responseAttributeNames = new ArrayList<String>();
				for (int i = 0; i < attributeStrings.length; i++) {
					String attr = attributeStrings[i];
					responseAttributeNames.add(attr);
				}
			}
		}
	}

	@Override
	protected void buildContextElementResponse(EntityId responseEntityId, ContextElementResponse contextElementResponse) {
		if (entityNotFound(responseEntityId)) {
			addNotFoundStatusCode(responseEntityId, contextElementResponse);
			addEntityIdOnlyContextElement(responseEntityId, contextElementResponse);
		} else {
			String notFoundAttribute = notFoundAttribute();
			if (null == notFoundAttribute) {
				addOkStatusCode(contextElementResponse);
				addFullContextElement(responseEntityId, contextElementResponse);
			} else {
				addAttributeNotFoundStatusCode(notFoundAttribute, contextElementResponse);
				addEntityIdAttributeContextElement(responseEntityId, contextElementResponse);
			}
		}
	}

	private boolean entityNotFound(EntityId responseEntityId) {
		return responseEntityId.getId().contains(Constants.NOT_FOUND);
	}

	private void addNotFoundStatusCode(EntityId responseEntityId, ContextElementResponse contextElementResponse) {
		String id = responseEntityId.getId();
		String detail = "Context entity with ID \"" + id
				+ "\" not found. *You* have provoked this error by"
				+ " specifying a entity ID containing the string \""
				+ Constants.NOT_FOUND + "\" in the request. (here: \""
				+ id + "\")";
		StatusCode statusCode = NgsiStatusCodes.ContextElementNotFound.toStatusCode(detail);
		contextElementResponse.setStatusCode(statusCode);
	}

	private void addEntityIdOnlyContextElement(EntityId responseEntityId, ContextElementResponse contextElementResponse) {
		ContextElement elem = contextElementResponse.addNewContextElement();
		addEntityId(responseEntityId, elem);
	}

	private String notFoundAttribute() {
		for (String responseAttributeName : responseAttributeNames) {
			if (responseAttributeName.contains(Constants.NOT_FOUND)) {
				return responseAttributeName;
			}
		}
		return null;
	}

	private void addAttributeNotFoundStatusCode(String notFoundAttribute, ContextElementResponse contextElementResponse) {
		String detail = "Context entity with attribute \"" + notFoundAttribute
				+ "\" not found. *You* have provoked this error by"
				+ " specifying an attribute containing the string \""
				+ Constants.NOT_FOUND + "\" in the request. (here: \""
				+ notFoundAttribute + "\")";
		StatusCode statusCode = NgsiStatusCodes.ContextElementNotFound.toStatusCode(detail);
		contextElementResponse.setStatusCode(statusCode);
	}

	private void addEntityIdAttributeContextElement(EntityId responseEntityId, ContextElementResponse contextElementResponse) {
		ContextElement elem = contextElementResponse.addNewContextElement();
		addEntityId(responseEntityId, elem);
		addAttributeNamesOnly(elem);
	}

	private void addAttributeNamesOnly(ContextElement elem) {
		ContextAttributeList contextAttributeList = elem.addNewContextAttributeList();
		if (responseAttributeNames.isEmpty()) {
			addAttributeNameOnly("temperature", contextAttributeList);
		} else {
			for (String attributeName : responseAttributeNames) {
				addAttributeNameOnly(attributeName, contextAttributeList);
			}
		}
	}

	private void addAttributeNameOnly(String attributeName, ContextAttributeList contextAttributeList) {
		ContextAttribute contextAttribute = contextAttributeList.addNewContextAttribute();
		contextAttribute.setName(attributeName);
	}

}
