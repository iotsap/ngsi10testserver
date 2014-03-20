package com.sap.research.fiware.ngsi10.test.contextsubscriptions;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import noNamespace.SubscribeContextRequestDocument;

import org.apache.xmlbeans.GDuration;
import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.NGSIRequestDocumentBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.SubscribeContextResponseChecker;
import com.sap.research.fiware.ngsi10.test.responsecheckers.SubscribeContextResponseCheckerBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.TopLevelElement;

// TODO: Tests for all different status codes as defined in OMA 5.5.14
/*
 * The convenience function POST .../contextSubscriptions doubles the standard
 * function POST .../subscribeContext, so both paths are tested similarly.
 * @see com.sap.research.fiware.ngsi10.test.subscribecontext.TestSubscribeContext
 */
public class TestInsertSubscription {

	@Test
	public void postOnContextSubscriptionsCompleteRequest() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 1, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 5, 0, null);
		SubscribeContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteSubscribeContextRequestDocument(duration, throttling);
		HttpURLConnection conn = buildPostSubscriptionRequest("application/xml", reqDoc.toString());
		SubscribeContextResponseChecker subscribeContextResponseChecker = buildOkSubscribeContextResponseChecker(duration, throttling);
		subscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnContextSubscriptionsWithNoContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostSubscriptionRequest(null, null);
		SubscribeContextResponseChecker subscribeContextResponseChecker = buildBadRequestSubscribeContextResponseChecker(415, "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"[no media type information in request]\".");
		subscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnContextSubscriptionsWithWrongContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostSubscriptionRequest("text/plain", null);
		SubscribeContextResponseChecker subscribeContextResponseChecker = buildBadRequestSubscribeContextResponseChecker(415, "media type does NOT EQUAL the required media type application/xml", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"text/plain\".");
		subscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnContextSubscriptionsWithoutContentMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostSubscriptionRequest("application/xml", null);
		SubscribeContextResponseChecker subscribeContextResponseChecker = buildBadRequestSubscribeContextResponseChecker(400, "This operation requires a message body, yet the payload is empty.");
		subscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnContextSubscriptionsEntityIdNotFoundMustResultInNotFound() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 1, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 5, 0, null);
		String requestedId = "urn:x-oma-application:dpe:345987:98759123" + "." + Constants.NOT_FOUND;
		List<String> requestedIds = new ArrayList<String>();
		requestedIds.add(requestedId);
		List<String> requestedAttributes = new ArrayList<String>();
		requestedAttributes.add("light");
		requestedAttributes.add("volume");
		SubscribeContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteSubscribeContextRequestDocument(requestedIds, requestedAttributes, duration, throttling);
		HttpURLConnection conn = buildPostSubscriptionRequest("application/xml", reqDoc.toString());
		SubscribeContextResponseChecker subscribeContextResponseChecker = SubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedErrorMessages("Context entity with id \"" + requestedId + "\" not found. *You* have provoked this error by specifying an id containing the string \"NOT_FOUND\" in the request.")
				.build();
		subscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnContextSubscriptionsAttributeNotFoundMustResultInNotFound() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 1, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 5, 0, null);
		List<String> requestedIds = new ArrayList<String>();
		requestedIds.add("urn:x-oma-application:dpe:345987:98759123");
		List<String> requestedAttributes = new ArrayList<String>();
		String requestedAttribute = "light" + "." + Constants.NOT_FOUND;
		requestedAttributes.add(requestedAttribute);
		SubscribeContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteSubscribeContextRequestDocument(requestedIds, requestedAttributes, duration, throttling);
		HttpURLConnection conn = buildPostSubscriptionRequest("application/xml", reqDoc.toString());
		SubscribeContextResponseChecker subscribeContextResponseChecker = SubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedErrorMessages("Context entity with attribute \"" + requestedAttribute + "\" not found. *You* have provoked this error by specifying an id containing the string \"NOT_FOUND\" in the request.")
				.build();
		subscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnContextSubscriptionsNotWellFormedMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostSubscriptionRequest("application/xml", "foo");
		SubscribeContextResponseChecker subscribeContextResponseChecker = buildBadRequestSubscribeContextResponseChecker(400, "Error parsing XML from payload:");
		subscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnContextSubscriptionsNotSchemaValidMustResultInBadRequest() throws Exception {
		SubscribeContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildIncompleteSubscribeContextRequestDocument();
		HttpURLConnection conn = buildPostSubscriptionRequest("application/xml", reqDoc.toString());
		SubscribeContextResponseChecker subscribeContextResponseChecker = buildBadRequestSubscribeContextResponseChecker(400, "The request body is not valid \"D=subscribeContextRequest\" according to the xsd schema definition.");
		subscribeContextResponseChecker.checkResponse(conn);
	}

	private HttpURLConnection buildPostSubscriptionRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.POST
				.url(Constants.URL_SUBSCRIPTIONS)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

	private SubscribeContextResponseChecker buildOkSubscribeContextResponseChecker(GDuration duration, GDuration throttling) {
		return SubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedDuration(duration)
				.expectedThrottling(throttling)
				.build();
	}

	private SubscribeContextResponseChecker buildBadRequestSubscribeContextResponseChecker(int expectedHTTPStatusCode, String... expectedErrorMessages) {
		return SubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(expectedHTTPStatusCode)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(400, "Bad request")
				.expectedErrorMessages(expectedErrorMessages)
				.build();
	}

}
