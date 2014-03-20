package com.sap.research.fiware.ngsi10.test.updatecontextsubscription;

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

/*
 * The convenience function PUT .../contextSubscriptions/{subscriptionID}
 * doubles the standard function POST .../updateContextSubscription, so both
 * paths are tested similarly.
 * @see com.sap.research.fiware.ngsi10.test.contextsubscriptions.TestUpdateSubscription
 */
public class TestUpdateContextSubscription {

	@Test
	public void postOnUpdateContextSubscriptionCompleteRequest() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 3, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 10, 0, null);
		UpdateContextSubscriptionRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteUpdateContextSubscriptionRequestDocument(duration, throttling);
		HttpURLConnection conn = buildPostUpdateContextSubscriptionRequest("application/xml", reqDoc.toString());
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildOkUpdateContextSubscriptionResponseChecker(duration, throttling);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextSubscriptionWithNoContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextSubscriptionRequest(null, null);
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 415, 400, "Bad request", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"[no media type information in request]\".", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextSubscriptionWithWrongContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextSubscriptionRequest("text/plain", null);
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 415, 400, "Bad request", "media type does NOT EQUAL the required media type application/xml", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"text/plain\".", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextSubscriptionWithoutContentMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextSubscriptionRequest("application/xml", null);
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 400, 400, "Bad request", "This operation requires a message body, yet the payload is empty.", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextSubscriptionIDNotFoundMustResultInNotFound() throws Exception {
		GDuration duration = new GDuration(+1, 0, 0, 3, 0, 0, 0, null);
		GDuration throttling = new GDuration(+1, 0, 0, 0, 0, 10, 0, null);
		UpdateContextSubscriptionRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteUpdateContextSubscriptionRequestDocument(duration, throttling);
		reqDoc.getUpdateContextSubscriptionRequest().setSubscriptionId(Constants.EXAMPLE_ID_NOT_FOUND);
		HttpURLConnection conn = buildPostUpdateContextSubscriptionRequest("application/xml", reqDoc.toString());
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.EXAMPLE_ID_NOT_FOUND, 200, 470, "Subscription ID not found", "Context Subscription with ID \"" + Constants.EXAMPLE_ID_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a Subscription ID containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextSubscriptionNotWellFormedMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextSubscriptionRequest("application/xml", "foo");
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 400, 400, "Bad request", "Error parsing XML from payload:", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextSubscriptionNotSchemaValidMustResultInBadRequest() throws Exception {
		UpdateContextSubscriptionRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildIncompleteUpdateRequestDocument();
		HttpURLConnection conn = buildPostUpdateContextSubscriptionRequest("application/xml", reqDoc.toString());
		UpdateContextSubscriptionResponseChecker updateContextSubscriptionResponseChecker = buildBadRequestUpdateContextSubscriptionResponseChecker(Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID, 400, 400, "Bad request", "The request body is not valid \"D=updateContextSubscriptionRequest\" according to the xsd schema definition.", Constants.UPDATECONTEXTSUBSCRIPTIONREQUEST_NO_SUBSCRIPTION_ID);
		updateContextSubscriptionResponseChecker.checkResponse(conn);
	}

	private HttpURLConnection buildPostUpdateContextSubscriptionRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.POST
				.url(Constants.URL_UDATE_CONTEXT_SUBSCRIPTION)
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
