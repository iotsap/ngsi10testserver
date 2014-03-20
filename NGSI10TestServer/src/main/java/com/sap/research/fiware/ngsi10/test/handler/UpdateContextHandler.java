package com.sap.research.fiware.ngsi10.test.handler;

import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextElementList;
import noNamespace.ContextElementResponse;
import noNamespace.ContextElementResponseList;
import noNamespace.EntityId;
import noNamespace.StatusCode;
import noNamespace.UpdateActionType;
import noNamespace.UpdateActionType.Enum;
import noNamespace.UpdateContextRequest;
import noNamespace.UpdateContextRequestDocument;
import noNamespace.UpdateContextResponse;
import noNamespace.UpdateContextResponseDocument;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;

import com.sap.research.fiware.ngsi10.test.helpers.Constants;
import com.sap.research.fiware.ngsi10.test.helpers.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.helpers.RequestTestHelper;
import com.sap.research.fiware.ngsi10.test.helpers.ResponseTestHelper;

public class UpdateContextHandler {

	private HttpHeaders headers;
	private UriInfo info;
	private Request requestStr;
	private String payload;
	private RequestTestHelper<UpdateContextRequestDocument> reqHlp;
	private ResponseTestHelper<UpdateContextResponseDocument> resHlp;
	private ContextElement[] requestContextElements;
	private Enum requestUpdateAction;
	private String contextElementStatusMessage;
	private NgsiStatusCodes contextElementStatusCode;

	public UpdateContextHandler(HttpHeaders headers, UriInfo info, Request requestStr, String payload) {
		this.headers = headers;
		this.info = info;
		this.requestStr = requestStr;
		this.payload = payload;
	}

	public Response handle() {
		reqHlp = initializeRequestHelper();
		resHlp = initializeResponseHelper();
		
		// try to parse request payload
		reqHlp.checkRequest();
		
		// handle request errors
		if (reqHlp.hasErrors()) {
			return resHlp.toRestResponse(reqHlp, UpdateContextResponseDocument.Factory.newInstance());
		}
		
		requestUpdateAction = extractUpdateAction(reqHlp);
		requestContextElements = extractContextElements(reqHlp);
		
		if (requestHasNoContextElements(requestContextElements)) {
			String description = "ContextElementList must not be empty";
			reqHlp.addNewIssueRecord(Status.BAD_REQUEST, description, NgsiStatusCodes.BadRequest);
			return resHlp.toRestResponse(reqHlp, UpdateContextResponseDocument.Factory.newInstance());
		}
		
		UpdateContextResponseDocument resDoc = buildResponse();
		
		String responseErrors = validateResponse(resDoc);
		if (null != responseErrors) {
			return Response.serverError().entity(responseErrors).build();
		}
		
		return Response.ok(resDoc.toString()).build();
	}

	private ResponseTestHelper<UpdateContextResponseDocument> initializeResponseHelper() {
		return new ResponseTestHelper<UpdateContextResponseDocument>();
	}

	private RequestTestHelper<UpdateContextRequestDocument> initializeRequestHelper() {
		RequestTestHelper<UpdateContextRequestDocument> reqHlp = new RequestTestHelper<UpdateContextRequestDocument>(requestStr, info, headers, payload);
		reqHlp.setRequiredMediaType(MediaType.APPLICATION_XML_TYPE);
		reqHlp.setAllowsQueryParams(false);
		reqHlp.setRequiresPayload(true);
		return reqHlp;
	}

	private Enum extractUpdateAction(RequestTestHelper<UpdateContextRequestDocument> reqHlp) {
		UpdateContextRequestDocument requestDoc = reqHlp.getReqDoc();
		UpdateContextRequest updateContextRequest = requestDoc.getUpdateContextRequest();
		return updateContextRequest.getUpdateAction();
	}

	private ContextElement[] extractContextElements(RequestTestHelper<UpdateContextRequestDocument> reqHlp) {
		UpdateContextRequestDocument requestDoc = reqHlp.getReqDoc();
		UpdateContextRequest updateContextRequest = requestDoc.getUpdateContextRequest();
		ContextElementList contextElementList = updateContextRequest.getContextElementList();
		return contextElementList.getContextElementArray();
	}

	private boolean requestHasNoContextElements(ContextElement[] requestContextElements) {
		return null == requestContextElements || 0 == requestContextElements.length;
	}

	private UpdateContextResponseDocument buildResponse() {
		UpdateContextResponseDocument resDoc = UpdateContextResponseDocument.Factory.newInstance();
		UpdateContextResponse resp = resDoc.addNewUpdateContextResponse();
		ContextElementResponseList contextElementResponseList = resp.addNewContextResponseList();
		for (int i = 0; i < requestContextElements.length; i++) {
			ContextElement requestContextElement = requestContextElements[i];
			addResponseElementForRequestElement(requestContextElement, contextElementResponseList);
		}
		return resDoc;
	}

	private String validateResponse(UpdateContextResponseDocument resDoc) {
		String responseErrors = null;
		ArrayList<String> errors = new ArrayList<String>();
		if (!resHlp.isValidResponse(resDoc, errors)) {
			StringBuilder sb = new StringBuilder();
			sb.append("Internal server error: Invalid response.\nErrors:");
			for (String error : errors) {
				sb.append("\n" + error);
			}
			responseErrors = sb.toString();
		}
		return responseErrors;
	}

	private void addResponseElementForRequestElement(ContextElement requestContextElement, ContextElementResponseList contextElementResponseList) {
		contextElementStatusMessage = null;
		contextElementStatusCode = NgsiStatusCodes.Ok;
		ContextAttributeList contextAttributeList = requestContextElement.getContextAttributeList();
		if (null == contextAttributeList) {
			addSCAttrListMissing(requestContextElement, contextElementResponseList);
			return;
		}
		ContextAttribute[] contextAttributes = contextAttributeList.getContextAttributeArray();
		if (null == contextAttributes || 0 == contextAttributes.length) {
			addSCAttrListMissing(requestContextElement, contextElementResponseList);
			return;
		}
		ContextElementResponse contextElementResponse = contextElementResponseList.addNewContextElementResponse();
		checkForErrors(requestContextElement);
		addContextElement(requestContextElement, contextAttributes, contextElementResponse);
		addStatusCode(contextElementResponse);
	}

	private void checkForErrors(ContextElement requestContextElement) {
		checkForContextElementsNotFound(requestContextElement);
		checkForAttributesNotFound(requestContextElement);
		checkForEmptyContextValues(requestContextElement);
	}

	private void checkForContextElementsNotFound(ContextElement requestContextElement) {
		if (requestedContextElementFound(requestContextElement)) {
			return;
		}
		if (UpdateActionType.APPEND == requestUpdateAction) {
			return;
		}
		String id = requestContextElement.getEntityId().getId();
		contextElementStatusMessage = "Context element with ID \"" + id + "\""
				+ " not found. *You* have provoked this error by specifying an"
				+ " element ID containing the string \"" + Constants.NOT_FOUND
				+ "\" in the request. (here: \"" + id + "\")";
		contextElementStatusCode = NgsiStatusCodes.ContextElementNotFound;
	}

	private boolean requestedContextElementFound(ContextElement requestContextElement) {
		return !requestedContextElementNotFound(requestContextElement);
	}

	private boolean requestedContextElementNotFound(ContextElement requestContextElement) {
		return requestContextElement.getEntityId().getId().contains(Constants.NOT_FOUND);
	}

	private void checkForAttributesNotFound(ContextElement requestContextElement) {
		String notFoundAttributeName = requestedContextAttributeNotFound(requestContextElement);
		if (null == notFoundAttributeName) {
			return;
		}
		if (UpdateActionType.APPEND == requestUpdateAction) {
			return;
		}
		contextElementStatusMessage = "Context attribute with name \""
				+ notFoundAttributeName + "\" not found. *You* have provoked"
				+ " this error by specifying an attribute name containing the"
				+ " string \"" + Constants.NOT_FOUND + "\" in the request."
				+ " (here: \"" + notFoundAttributeName + "\")";
		contextElementStatusCode = NgsiStatusCodes.ContextElementNotFound;
	}

	private String requestedContextAttributeNotFound(ContextElement requestContextElement) {
		ContextAttribute[] contextAttributes = requestContextElement.getContextAttributeList().getContextAttributeArray();
		for (ContextAttribute contextAttribute : contextAttributes) {
			if (contextAttribute.getName().contains(Constants.NOT_FOUND)) {
				return contextAttribute.getName();
			}
		}
		return null;
	}

	private void checkForEmptyContextValues(ContextElement requestContextElement) {
		String attributeWithEmptyValue = findAttributeWithEmptyValue(requestContextElement);
		if (null == attributeWithEmptyValue) {
			return;
		}
		if (UpdateActionType.DELETE == requestUpdateAction) {
			return;
		}
		contextElementStatusMessage = "The context value of attribute \""
				+ attributeWithEmptyValue + "\" must not be empty for"
				+ " update action \"" + requestUpdateAction + "\".";
		contextElementStatusCode = NgsiStatusCodes.MissingParameter;
	}

	private String findAttributeWithEmptyValue(ContextElement requestContextElement) {
		ContextAttribute[] contextAttributes = requestContextElement.getContextAttributeList().getContextAttributeArray();
		for (ContextAttribute contextAttribute : contextAttributes) {
			XmlObject contextValue = contextAttribute.getContextValue();
			if (contextValue instanceof XmlAnyTypeImpl) {
				String theRealValue = ((XmlAnyTypeImpl) contextValue).getStringValue();
				if (null == theRealValue || 0 == theRealValue.length()) {
					return contextAttribute.getName();
				}
			} else {
				return contextAttribute.getName();
			}
		}
		return null;
	}

	private void addContextElement(ContextElement requestContextElement, ContextAttribute[] contextAttributes, ContextElementResponse contextElementResponse) {
		ContextElement contextElement = contextElementResponse.addNewContextElement();
		EntityId entityId = contextElement.addNewEntityId();
		entityId.setId(requestContextElement.getEntityId().getId());
		ContextAttributeList responseContextAttributeList = contextElement.addNewContextAttributeList();
		for (int i = 0; i < contextAttributes.length; i++) {
			ContextAttribute contextAttribute = contextAttributes[i];
			ContextAttribute responseContextAttribute = responseContextAttributeList.addNewContextAttribute();
			responseContextAttribute.setName(contextAttribute.getName());
			responseContextAttribute.setContextValue(contextAttribute.getContextValue());
		}
	}

	private void addStatusCode(ContextElementResponse contextElementResponse) {
		if (null == contextElementStatusMessage) {
			contextElementStatusMessage = reqHlp.getRequestInformation();
		}
		contextElementResponse.setStatusCode(contextElementStatusCode.toStatusCode(contextElementStatusMessage));
	}

	private void addSCAttrListMissing(ContextElement requestContextElement, ContextElementResponseList contextElementResponseList) {
		ContextElementResponse contextElementResponse = contextElementResponseList.addNewContextElementResponse();
		ContextElement contextElement = contextElementResponse.addNewContextElement();
		EntityId entityId = contextElement.addNewEntityId();
		entityId.setId(requestContextElement.getEntityId().getId());
		StatusCode statusCode = NgsiStatusCodes.AttributeListRequired.toStatusCode("Attribute List for Entity " + requestContextElement.getEntityId().getId() + " is missing");
		contextElementResponse.setStatusCode(statusCode);
	}

}
