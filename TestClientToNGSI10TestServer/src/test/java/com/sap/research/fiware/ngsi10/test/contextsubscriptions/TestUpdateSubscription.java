package com.sap.research.fiware.ngsi10.test.contextsubscriptions;

import java.net.HttpURLConnection;

import noNamespace.UpdateContextSubscriptionRequestDocument;

import org.apache.xmlbeans.GDuration;
import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.NGSIRequestDocumentBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.TopLevelElement;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UpdateContextSubscriptionResponseChecker;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UpdateContextSubscriptionResponseCheckerBuilder;

//TODO: Tests for all different status codes as defined in OMA 5.5.14
/*
 * The convenience function PUT .../contextSubscriptions/{subscriptionID}
 * doubles the standard function POST .../updateContextSubscription, so both
 * paths are tested similarly.
 * @see com.sap.research.fiware.ngsi10.test.updatecontextsubscription.TestUpdateContextSubscription
 */
public class TestUpdateSubscription {

	@Test
	public void putOnContextSubscriptionIDCompleteRequest() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 3, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 10, 0, null);
		UpdateContextSubscriptionRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteUpdateContextSubscriptionRequestDocument(duration, throttling);
		HttpURLConnection conn = buildPutSubscriptionIDRequest("application/xml", reqDoc.toString());
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildOkUpdateContextSubscriptionResponseChecker(duration, throttling);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void putOnContextSubscriptionIDWithNoContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPutSubscriptionIDRequest(null, null);
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 415, 400, "Bad request", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"[no media type information in request]\".", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void putOnContextSubscriptionIDWithWrongContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPutSubscriptionIDRequest("text/plain", null);
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 415, 400, "Bad request", "media type does NOT EQUAL the required media type application/xml", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"text/plain\".", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void putOnContextSubscriptionIDWithoutContentMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPutSubscriptionIDRequest("application/xml", null);
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 400, 400, "Bad request", "This operation requires a message body, yet the payload is empty.", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void putOnContextSubscriptionIDNotFoundMustResultInNotFound() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 3, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 10, 0, null);
		UpdateContextSubscriptionRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteUpdateContextSubscriptionRequestDocument(duration, throttling);
		reqDoc.getUpdateContextSubscriptionRequest().setSubscriptionId(Constants.EXAMPLE_ID_NOT_FOUND);
		HttpURLConnection conn = buildPutSubscriptionIDNotFoundRequest("application/xml", reqDoc.toString());
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.EXAMPLE_ID_NOT_FOUND, 404, 470, "Subscription ID not found", "Context Subscription with ID \"" + Constants.EXAMPLE_ID_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a Subscription ID containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void putOnContextSubscriptionIDNotWellFormedMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPutSubscriptionIDRequest("application/xml", "foo");
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 400, 400, "Bad request", "Error parsing XML from payload:", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void putOnContextSubscriptionIDNotSchemaValidMustResultInBadRequest() throws Exception {
		UpdateContextSubscriptionRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildIncompleteUpdateRequestDocument();
		HttpURLConnection conn = buildPutSubscriptionIDRequest("application/xml", reqDoc.toString());
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 400, 400, "Bad request", "The request body is not valid \"D=updateContextSubscriptionRequest\" according to the xsd schema definition.", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void putOnContextSubscriptionIDDifferentIDsMustResultInBadRequest() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 3, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 10, 0, null);
		UpdateContextSubscriptionRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteUpdateContextSubscriptionRequestDocument(duration, throttling);
		String wrongSubscriptionId = "wrongSubscriptionId";
		reqDoc.getUpdateContextSubscriptionRequest().setSubscriptionId(wrongSubscriptionId);
		HttpURLConnection conn = buildPutSubscriptionIDRequest("application/xml", reqDoc.toString());
		String expectedID = "Subscription IDs in request URL and request payload differ: '" + Constants.EXAMPLE_ID + "' != '" + wrongSubscriptionId + "'";
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(expectedID, 400, 400, "Bad request", expectedID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	private HttpURLConnection buildPutSubscriptionIDRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_SUBSCRIPTION_ID)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

	private HttpURLConnection buildPutSubscriptionIDNotFoundRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_SUBSCRIPTION_ID_NOT_FOUND)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

	private UpdateContextSubscriptionResponseChecker buildOkUpdateContextSubscriptionResponseChecker(GDuration duration, GDuration throttling) {
		return UpdateContextSubscriptionResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedID(Constants.EXAMPLE_ID)
				.expectedDuration(duration)
				.expectedThrottling(throttling)
				.build();
	}

	private UpdateContextSubscriptionResponseChecker buildBadRequestUpdateContextSubscriptionResponseChecker(String expectedID, int expectedHTTPStatusCode, int expectedNGSIStatusCode, String expectedNGSIReasonPhrase, String... expectedErrorMessages) {
		return UpdateContextSubscriptionResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(expectedHTTPStatusCode)
				.expectedID(expectedID)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(expectedNGSIStatusCode, expectedNGSIReasonPhrase)
				.expectedErrorMessages(expectedErrorMessages)
				.build();
	}

}
