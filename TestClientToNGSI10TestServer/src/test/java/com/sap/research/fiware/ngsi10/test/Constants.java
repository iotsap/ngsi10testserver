package com.sap.research.fiware.ngsi10.test;

import com.google.api.client.escape.PercentEscaper;

public class Constants {

	private static final PercentEscaper pe = new PercentEscaper(PercentEscaper.SAFEPATHCHARS_URLENCODER, false);

	/*
	 * Local Testing.
	 */
	public static final String SERVER = "http://localhost:8080/";
	/*
	 * Due to SAP's corporate proxy, you will need to switch from SAP-Corporate
	 * to SAP-Internet in order to test the installation on iot4bpm.de.
	 */
	// public static final String SERVER = "http://modeler.iot4bpm.de/";
	public static final String NGSI10_PATH = "NGSI10/";
	public static final String NGSI10_BASE_URL = SERVER + NGSI10_PATH;

	// constants for provoking 404 or 409 errors
	public static final String NOT_FOUND = "NOT_FOUND";
	public static final String EXISTING = "EXISTING";

	// query context

	public static final String PATH_QUERY_CONTEXT = "queryContext/";

	// http://{serverRoot}/{apiVersion}/queryContext
	public static final String URL_QUERY_CONTEXT = NGSI10_BASE_URL + PATH_QUERY_CONTEXT;

	// subscribe context

	public static final String PATH_SUBSCRIBE_CONTEXT = "subscribeContext/";

	// http://{serverRoot}/{apiVersion}/subscribeContext
	public static final String URL_SUBSCRIBE_CONTEXT = NGSI10_BASE_URL + PATH_SUBSCRIBE_CONTEXT;

	// update context subscription

	public static final String PATH_UDATE_CONTEXT_SUBSCRIPTION = "updateContextSubscription/";

	// http://{serverRoot}/{apiVersion}/updateContextSubscription
	public static final String URL_UDATE_CONTEXT_SUBSCRIPTION = NGSI10_BASE_URL + PATH_UDATE_CONTEXT_SUBSCRIPTION;

	// unsubscribe context

	public static final String PATH_UNSUBSCRIBE_CONTEXT = "unsubscribeContext/";

	// http://{serverRoot}/{apiVersion}/unsubscribeContext
	public static final String URL_UNSUBSCRIBE_CONTEXT = NGSI10_BASE_URL + PATH_UNSUBSCRIBE_CONTEXT;

	// update context

	public static final String PATH_UPDATE_CONTEXT = "updateContext/";

	// http://{serverRoot}/{apiVersion}/updateContext
	public static final String URL_UPDATE_CONTEXT = NGSI10_BASE_URL + PATH_UPDATE_CONTEXT;

	// context entity types

	public static final String PATH_ENTITY_TYPES = "contextEntityTypes/";

	public static final String EXAMPLE_TYPE_NAME = "http://frc.research.sap.com/semantic#Crate";
	public static final String EXAMPLE_TYPE_NAME_PERCENT_ENCODED = pe.escape(EXAMPLE_TYPE_NAME);
	public static final String EXAMPLE_TYPE_NAME_NOT_FOUND = EXAMPLE_TYPE_NAME + "." + NOT_FOUND;
	public static final String EXAMPLE_TYPE_NAME_NOT_FOUND_PERCENT_ENCODED = pe.escape(EXAMPLE_TYPE_NAME_NOT_FOUND);

	public static final String URL_ENTITY_TYPES = NGSI10_BASE_URL + PATH_ENTITY_TYPES;

	// http://{serverRoot}/{apiVersion}/contextEntityTypes/{typeName}
	public static final String URL_EXAMPLE_TYPE_NAME = URL_ENTITY_TYPES + EXAMPLE_TYPE_NAME_PERCENT_ENCODED;
	public static final String URL_EXAMPLE_TYPE_NAME_NOT_FOUND = URL_ENTITY_TYPES + EXAMPLE_TYPE_NAME_NOT_FOUND_PERCENT_ENCODED;

	public static final String ATTRIBUTES = "/attributes";

	// http://{serverRoot}/{apiVersion}/contextEntityTypes/{typeName}/attributes
	public static final String URL_EXAMPLE_TYPE_NAME_ATTRIBUTES = URL_EXAMPLE_TYPE_NAME + ATTRIBUTES;
	public static final String URL_EXAMPLE_TYPE_NAME_NOT_FOUND_ATTRIBUTES = URL_EXAMPLE_TYPE_NAME_NOT_FOUND + ATTRIBUTES;

	public static final String EXAMPLE_ATTRIBUTE_NAME = "relative humidity";
	public static final String EXAMPLE_ATTRIBUTE_NAME_PERCENT_ENCODED = pe.escape(EXAMPLE_ATTRIBUTE_NAME);
	public static final String EXAMPLE_ATTRIBUTE_NAME_NOT_FOUND = EXAMPLE_ATTRIBUTE_NAME + "." + NOT_FOUND;
	public static final String EXAMPLE_ATTRIBUTE_NAME_NOT_FOUND_PERCENT_ENCODED = pe.escape(EXAMPLE_ATTRIBUTE_NAME_NOT_FOUND);

	// http://{serverRoot}/{apiVersion}/contextEntityTypes/{typeName}/attributes/{attributeName}
	public static final String URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME = URL_EXAMPLE_TYPE_NAME_ATTRIBUTES + "/"
			+ EXAMPLE_ATTRIBUTE_NAME_PERCENT_ENCODED;
	public static final String URL_EXAMPLE_TYPE_NAME_NOT_FOUND_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME = URL_EXAMPLE_TYPE_NAME_NOT_FOUND_ATTRIBUTES + "/"
			+ EXAMPLE_ATTRIBUTE_NAME_PERCENT_ENCODED;
	public static final String URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME_NOT_FOUND = URL_EXAMPLE_TYPE_NAME_ATTRIBUTES + "/"
			+ EXAMPLE_ATTRIBUTE_NAME_NOT_FOUND_PERCENT_ENCODED;

	public static final String EXAMPLE_ATTRIBUTE_DOMAIN_NAME = "device info";
	public static final String EXAMPLE_ATTRIBUTE_DOMAIN_NAME_PERCENT_ENCODED = pe.escape(EXAMPLE_ATTRIBUTE_DOMAIN_NAME);
	public static final String EXAMPLE_ATTRIBUTE_DOMAIN_NAME_NOT_FOUND = EXAMPLE_ATTRIBUTE_DOMAIN_NAME + "." + NOT_FOUND;
	public static final String EXAMPLE_ATTRIBUTE_DOMAIN_NAME_NOT_FOUND_PERCENT_ENCODED = pe.escape(EXAMPLE_ATTRIBUTE_DOMAIN_NAME_NOT_FOUND);

	// http://{serverRoot}/{apiVersion}/contextEntityTypes/{typeName}/attributeDomains/{attributeDomainName}
	public static final String URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME = URL_EXAMPLE_TYPE_NAME + "/attributeDomains/"
			+ EXAMPLE_ATTRIBUTE_DOMAIN_NAME_PERCENT_ENCODED;
	public static final String URL_EXAMPLE_TYPE_NAME_NOT_FOUND_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME = URL_EXAMPLE_TYPE_NAME_NOT_FOUND + "/attributeDomains/"
			+ EXAMPLE_ATTRIBUTE_DOMAIN_NAME_PERCENT_ENCODED;
	public static final String URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME_NOT_FOUND = URL_EXAMPLE_TYPE_NAME + "/attributeDomains/"
			+ EXAMPLE_ATTRIBUTE_DOMAIN_NAME_NOT_FOUND_PERCENT_ENCODED;

	// invalid type name (not a valid URI)
	public static final String EXAMPLE_TYPE_NAME_INVALID = "http:\\\\frc.research.sap.com/semantic#Crate";
	public static final String EXAMPLE_TYPE_NAME_INVALID_PERCENT_ENCODED = pe.escape(EXAMPLE_TYPE_NAME_INVALID);
	public static final String URL_EXAMPLE_TYPE_NAME_INVALID = URL_ENTITY_TYPES
			+ EXAMPLE_TYPE_NAME_INVALID_PERCENT_ENCODED;
	public static final String URL_EXAMPLE_TYPE_NAME_INVALID_ATTRIBUTES = URL_EXAMPLE_TYPE_NAME_INVALID + ATTRIBUTES;
	public static final String URL_EXAMPLE_TYPE_NAME_INVALID_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME = URL_EXAMPLE_TYPE_NAME_INVALID_ATTRIBUTES + "/"
			+ EXAMPLE_ATTRIBUTE_NAME_PERCENT_ENCODED;
	public static final String URL_EXAMPLE_TYPE_NAME_INVALID_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME = URL_EXAMPLE_TYPE_NAME_INVALID
			+ "/attributeDomains/" + EXAMPLE_ATTRIBUTE_DOMAIN_NAME_PERCENT_ENCODED;

	// subscriptions

	public static final String PATH_SUBSCRIPTIONS = "contextSubscriptions/";

	public static final String EXAMPLE_ID = "123";
	public static final String EXAMPLE_ID_NOT_FOUND = EXAMPLE_ID + "." + NOT_FOUND;

	// http://{serverRoot}/{apiVersion}/contextSubscriptions
	public static final String URL_SUBSCRIPTIONS = NGSI10_BASE_URL + PATH_SUBSCRIPTIONS;

	// http://{serverRoot}/{apiVersion}/contextSubscriptions/{subscriptionID}
	public static final String URL_EXAMPLE_SUBSCRIPTION_ID = URL_SUBSCRIPTIONS + EXAMPLE_ID;
	public static final String URL_EXAMPLE_SUBSCRIPTION_ID_NOT_FOUND = URL_SUBSCRIPTIONS + EXAMPLE_ID_NOT_FOUND;

	// default values used by server implementation
	public static final String DEFAULT_ID = "Crate1";
	public static final String DEFAULT_ATTRIBUTE_NAME = "temperature";
	public static final String DEFAULT_ATTRIBUTE_DOMAIN_NAME = "enviromental parameters";

	public static final String UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID = "UpdateContextSubscriptionRequest contains no subscription ID.";
	public static final String UNSUBSCRIBECONTEXTREQUEST_NO_SUBSCRIPTION_ID = "UnsubscribeContextRequest contains no subscription ID.";

	//
	// olli* start
	//

	public static final String CONTENT_TYPE_NONE = null;
	public static final String CONTENT_TYPE_XML = "application/xml";
	public static final String CONTENT_TYPE_TEXT = "application/text";
	public static final String CONTENT_TYPE_JSON = "application/json";

	// context entities
	public static final String PATH_CONTEXT_ENTITIES = "contextEntities/";
	public static final String PATH_CONTEXT_ENTITIES_ATTRIBUTES = "attributes/";
	public static final String PATH_CONTEXT_ENTITIES_ATTRIBUTE_DOMAINS = "attributeDomains/";

	// examples
	public static final String EXAMPLE_ENTITY_ID = "room_3";
	// public static final String EXAMPLE_ATTRIBUTE_NAME = "temperature";
	public static final String EXAMPLE_VALUE_ID = "0815";

	// context entities/id/attributes/attribute name

	// http://{serverRoot}/{apiVersion}/contextSubscriptions
	// public static final String URL_SUBSCRIPTIONS = NGSI10_BASE_URL +
	// PATH_SUBSCRIPTIONS;

	// http://{serverRoot}/{apiVersion}/contextEntities/{EntityID}
	public static final String URL_EXAMPLE_ENTITY_ID = NGSI10_BASE_URL + PATH_CONTEXT_ENTITIES + EXAMPLE_ENTITY_ID;
	public static final String URL_EXAMPLE_ENTITY_ID_NOT_FOUND = NGSI10_BASE_URL + PATH_CONTEXT_ENTITIES
			+ EXAMPLE_ENTITY_ID + "." + NOT_FOUND;
	public static final String URL_EXAMPLE_ENTITY_ID_EXISTING = NGSI10_BASE_URL + PATH_CONTEXT_ENTITIES
			+ EXAMPLE_ENTITY_ID + "." + EXISTING;

	// http://{serverRoot}/{apiVersion}/contextEntities/{EntityID}/attributes
	public static final String URL_EXAMPLE_ENTITY_ID_ATTRIBUTES = NGSI10_BASE_URL + PATH_CONTEXT_ENTITIES
			+ EXAMPLE_ENTITY_ID + "/" + PATH_CONTEXT_ENTITIES_ATTRIBUTES;

	// http://{serverRoot}/{apiVersion}/contextEntities/{EntityID}/attributes/{attributeName}
	public static final String URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_NAME = NGSI10_BASE_URL + PATH_CONTEXT_ENTITIES
			+ EXAMPLE_ENTITY_ID + "/" + PATH_CONTEXT_ENTITIES_ATTRIBUTES + EXAMPLE_ATTRIBUTE_NAME_PERCENT_ENCODED;

	// http://{serverRoot}/{apiVersion}/contextEntities/{EntityID}/attributes/{attributeName}/{valueID}
	public static final String URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_NAME_VALUE_ID = NGSI10_BASE_URL + PATH_CONTEXT_ENTITIES
			+ EXAMPLE_ENTITY_ID + "/" + PATH_CONTEXT_ENTITIES_ATTRIBUTES + EXAMPLE_ATTRIBUTE_NAME_PERCENT_ENCODED + "/"
			+ EXAMPLE_VALUE_ID;

	// http://{serverRoot}/{apiVersion}/contextEntities/{EntityID}/attributeDomains/{attributeDomainName}
	public static final String URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_DOMAIN_NAME = NGSI10_BASE_URL + PATH_CONTEXT_ENTITIES
			+ EXAMPLE_ENTITY_ID + "/" + PATH_CONTEXT_ENTITIES_ATTRIBUTE_DOMAINS
			+ EXAMPLE_ATTRIBUTE_DOMAIN_NAME_PERCENT_ENCODED;

	//
	// olli* end
	//
}
