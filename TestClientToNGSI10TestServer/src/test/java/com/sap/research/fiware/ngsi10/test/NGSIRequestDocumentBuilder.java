package com.sap.research.fiware.ngsi10.test;

import java.util.ArrayList;
import java.util.List;

import noNamespace.AttributeList;
import noNamespace.CondValueList;
import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextElementList;
import noNamespace.EntityId;
import noNamespace.EntityIdList;
import noNamespace.NotifyCondition;
import noNamespace.NotifyConditionList;
import noNamespace.NotifyConditionType;
import noNamespace.OperationScope;
import noNamespace.OperationScopeList;
import noNamespace.QueryContextRequest;
import noNamespace.QueryContextRequestDocument;
import noNamespace.Restriction;
import noNamespace.SubscribeContextRequest;
import noNamespace.SubscribeContextRequestDocument;
import noNamespace.UnsubscribeContextRequest;
import noNamespace.UnsubscribeContextRequestDocument;
import noNamespace.UpdateActionType;
import noNamespace.UpdateContextRequest;
import noNamespace.UpdateContextRequestDocument;
import noNamespace.UpdateContextSubscriptionRequest;
import noNamespace.UpdateContextSubscriptionRequestDocument;

import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlString;

public class NGSIRequestDocumentBuilder {

	public static SubscribeContextRequestDocument buildCompleteSubscribeContextRequestDocument(GDuration duration, GDuration throttling) {
		List<String> reqestedEntityIds = new ArrayList<String>();
		reqestedEntityIds.add("urn:x-oma-application:dpe:345987:98759123");
		reqestedEntityIds.add("urn:x-oma-application:dpe:836527:23856129");
		List<String> requestedAttributes = new ArrayList<String>();
		// TODO: How do we distinct ContextAttributes and AttributeDomains here?
		requestedAttributes.add("temperature");
		requestedAttributes.add("humidity");
		return buildCompleteSubscribeContextRequestDocument(reqestedEntityIds, requestedAttributes, duration, throttling);
	}

	public static SubscribeContextRequestDocument buildCompleteSubscribeContextRequestDocument(List<String> requestedEntityIds, List<String> requestedAttributes, GDuration duration, GDuration throttling) {
		SubscribeContextRequestDocument reqDoc = SubscribeContextRequestDocument.Factory.newInstance();
		SubscribeContextRequest req = reqDoc.addNewSubscribeContextRequest();
		
		// TODO: Check if server returns 471 on missing EntityIdList
		buildIdList(req.addNewEntityIdList(), requestedEntityIds);
		
		// TODO: Check if server returns 482 if AttributeList is missing
		buildAttributeList(req.addNewAttributeList(), requestedAttributes);
		
		// TODO: Check if server returns 471 on missing Reference
		// TODO: Reference is xsd:anyURI in specification and xsd:anyType in XSD file
		req.setReference(XmlAnyURI.Factory.newValue("http://123.45.67.89:12345/foo"));
		
		// TODO: Test if server rejects negative durations
		// TODO: If server requires duration check if it rejects requests without
		// TODO: If no duration is given, implement both cases: server includes one in response and server doesn't do so
		req.setDuration(duration);
		
		buildCompleteRestriction(req.addNewRestriction());
		
		buildCompleteNotifyConditionList(req.addNewNotifyConditions());
		
		// TODO: What should happen on negative values?
		req.setThrottling(throttling);
		
		return reqDoc;
	}

	public static SubscribeContextRequestDocument buildIncompleteSubscribeContextRequestDocument() {
		SubscribeContextRequestDocument reqDoc = SubscribeContextRequestDocument.Factory.newInstance();
		SubscribeContextRequest req = reqDoc.addNewSubscribeContextRequest();
		EntityIdList idList = req.addNewEntityIdList();
		idList.addNewEntityId();
		return reqDoc;
	}

	public static UpdateContextSubscriptionRequestDocument buildCompleteUpdateContextSubscriptionRequestDocument(GDuration duration, GDuration throttling) {
		UpdateContextSubscriptionRequestDocument reqDoc = UpdateContextSubscriptionRequestDocument.Factory.newInstance();
		UpdateContextSubscriptionRequest req = reqDoc.addNewUpdateContextSubscriptionRequest();
		
		// TODO: if duration is not given in update, but it was given in subscribe before, then it must appear in the update response.
		req.setDuration(duration);
		
		buildCompleteRestriction(req.addNewRestriction());
		
		// TODO: Server must check that subscriptionId in request is equal to subscriptionId in path
		req.setSubscriptionId(Constants.EXAMPLE_ID);
		
		buildCompleteNotifyConditionList(req.addNewNotifyConditions());
		
		// TODO: if throttling is not given in update, but it was given in subscribe before, then it must appear in the update response.
		req.setThrottling(throttling);
		
		return reqDoc;
	}

	public static UpdateContextSubscriptionRequestDocument buildIncompleteUpdateRequestDocument() {
		UpdateContextSubscriptionRequestDocument reqDoc = UpdateContextSubscriptionRequestDocument.Factory.newInstance();
		UpdateContextSubscriptionRequest req = reqDoc.addNewUpdateContextSubscriptionRequest();
		req.setDuration(new GDuration(+1, 0, 0, 2, 0, 0, 0, null));
		return reqDoc;
	}

	public static UnsubscribeContextRequestDocument buildCompleteUnsubscribeContextRequestDocument() {
		UnsubscribeContextRequestDocument reqDoc = UnsubscribeContextRequestDocument.Factory.newInstance();
		UnsubscribeContextRequest req = reqDoc.addNewUnsubscribeContextRequest();
		req.setSubscriptionId(Constants.EXAMPLE_ID);
		return reqDoc;
	}

	public static UnsubscribeContextRequestDocument buildIncompleteUnsubscribeContextRequestDocument() {
		UnsubscribeContextRequestDocument reqDoc = UnsubscribeContextRequestDocument.Factory.newInstance();
		reqDoc.addNewUnsubscribeContextRequest();
		return reqDoc;
	}

	private static void buildCompleteRestriction(Restriction restriction) {
		// TODO: Check if server returns 471 on Restriction without AttributeExpression
		// TODO: Check if server checks that this string is a valid XPath expression
		// TODO: Reasonable example
		restriction.setAttributeExpression("/some/fancy/path");
		OperationScopeList scopeList = restriction.addNewScope();
		OperationScope scope1 = scopeList.addNewOperationScope();
		scope1.setScopeType("SimpleScope");
		// TODO: Reasonable example value
		scope1.setScopeValue(XmlString.Factory.newValue("scopeValue1"));
		OperationScope scope2 = scopeList.addNewOperationScope();
		scope2.setScopeType("SimpleScope");
		// TODO: Reasonable example value
		scope2.setScopeValue(XmlString.Factory.newValue("scopeValue2"));
		// TODO: Add more scope examples following the descriptions in OMA Appendix D.1 - D.4
	}

	private static void buildCompleteNotifyConditionList(NotifyConditionList conditionList) {
		// TODO: Test condition none of ONTIMEINTERVAL, ONVALUE or ONCHANGE
		NotifyCondition condition1 = conditionList.addNewNotifyCondition();
		condition1.setType(NotifyConditionType.ONTIMEINTERVAL);
		// TODO: Test zero or more than one condition values for ONTIMEINTERVAL
		CondValueList cond1ValList = condition1.addNewCondValueList();
		// TODO: Test value is no time interval
		// TODO: Why is this interval represented as string and not as duration?
		cond1ValList.addCondValue("30s");
		// TODO: Test restriction present for ONTIMEINTERVAL
		NotifyCondition condition2 = conditionList.addNewNotifyCondition();
		condition2.setType(NotifyConditionType.ONCHANGE);
		// TODO: Test no condition value for ONCHANGE
		CondValueList cond2ValList = condition2.addNewCondValueList();
		cond2ValList.addCondValue("temperature");
		cond2ValList.addCondValue("brightness");
		// TODO: Test restriction present for ONCHANGE
		NotifyCondition condition3 = conditionList.addNewNotifyCondition();
		condition3.setType(NotifyConditionType.ONVALUE);
		// TODO: Test condition value present for ONVALUE
		// TODO: Test no restriction present for ONVALUE
		// TODO: Test no valid XPath expression
		condition3.setRestriction("/some/fancy/restriction");
	}

	public static QueryContextRequestDocument buildCompleteQueryContextRequestDocument(List<String> reqestedEntityIds, List<String> requestedAttributes) {
		QueryContextRequestDocument reqDoc = QueryContextRequestDocument.Factory.newInstance();
		QueryContextRequest req = reqDoc.addNewQueryContextRequest();
		
		buildIdList(req.addNewEntityIdList(), reqestedEntityIds);
		
		buildAttributeList(req.addNewAttributeList(), requestedAttributes);
		
		buildCompleteRestriction(req.addNewRestriction());
		
		return reqDoc;
	}

	private static void buildIdList(EntityIdList idList, List<String> reqestedEntityIds) {
		for (String reqestedEntityId : reqestedEntityIds) {
			EntityId id = idList.addNewEntityId();
			id.setId(reqestedEntityId);
		}
	}

	private static void buildAttributeList(AttributeList attributeList, List<String> requestedAttributes) {
		for (String requestedAttribute : requestedAttributes) {
			attributeList.addAttribute(requestedAttribute);
		}
	}

	public static QueryContextRequestDocument buildIncompleteQueryContextRequestDocument() {
		QueryContextRequestDocument reqDoc = QueryContextRequestDocument.Factory.newInstance();
		QueryContextRequest req = reqDoc.addNewQueryContextRequest();
		
		buildCompleteRestriction(req.addNewRestriction());
		
		return reqDoc;
	}

	public static UpdateContextRequestDocument buildUpdateContextRequestDocument(List<String[]> requestedUpdates, UpdateActionType.Enum updateActionType) {
		UpdateContextRequestDocument reqDoc = UpdateContextRequestDocument.Factory.newInstance();
		UpdateContextRequest req = reqDoc.addNewUpdateContextRequest();
		
		buildContextElementList(req.addNewContextElementList(), requestedUpdates);
		
		req.setUpdateAction(updateActionType);
		
		return reqDoc;
	}

	private static void buildContextElementList(ContextElementList contextElementList, List<String[]> requestedUpdates) {
		for (String[] requestedUpdate : requestedUpdates) {
			ContextElement firstContextElement = contextElementList.addNewContextElement();
			EntityId entityId = firstContextElement.addNewEntityId();
			entityId.setId(requestedUpdate[0]);
			ContextAttributeList contextAttributeList = firstContextElement.addNewContextAttributeList();
			ContextAttribute contextAttribute = contextAttributeList.addNewContextAttribute();
			contextAttribute.setName(requestedUpdate[1]);
			contextAttribute.setContextValue(XmlString.Factory.newValue(requestedUpdate[2]));
		}
	}

	public static UpdateContextRequestDocument buildIncompleteUpdateContextRequestDocument() {
		UpdateContextRequestDocument reqDoc = UpdateContextRequestDocument.Factory.newInstance();
		UpdateContextRequest req = reqDoc.addNewUpdateContextRequest();
		
		req.setUpdateAction(UpdateActionType.UPDATE);
		
		return reqDoc;
	}

}
