package com.sap.research.fiware.ngsi10.test.unsubscribecontext;

import java.net.HttpURLConnection;

import noNamespace.UnsubscribeContextRequestDocument;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.NGSIRequestDocumentBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UnsubscribeContextResponseChecker;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UnsubscribeContextResponseCheckerBuilder;

/*
 * The convenience function DELETE .../contextSubscriptions/{subscriptionID}
 * doubles the standard function POST .../unsubscribeContext, so both paths are
 * tested similarly.
 * @see com.sap.research.fiware.ngsi10.test.contextsubscriptions.TestDeleteSubscription
 */
public class TestUnsubscribeContext {

	@Test
	public void postOnUnsubscribeContextCompleteRequest() throws Exception {
		UnsubscribeContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteUnsubscribeContextRequestDocument();
		HttpURLConnection conn = buildPostUnsubscribeContextRequest("application/xml", reqDoc.toString());
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildOkUnsubscribeContextResponseChecker();
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUnsubscribeContextWithNoContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUnsubscribeContextRequest(null, null);
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildBadRequestUnsubscribeContextResponseChecker(415, "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"[no media type information in request]\".");
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUnsubscribeContextWithWrongContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUnsubscribeContextRequest("text/plain", null);
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildBadRequestUnsubscribeContextResponseChecker(415, "media type does NOT EQUAL the required media type application/xml", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"text/plain\".");
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUnsubscribeContextWithoutContentMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUnsubscribeContextRequest("application/xml", null);
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildBadRequestUnsubscribeContextResponseChecker(400, "This operation requires a message body, yet the payload is empty.");
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUnsubscribeContextIDNotFoundMustResultInNotFound() throws Exception {
		UnsubscribeContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteUnsubscribeContextRequestDocument();
		reqDoc.getUnsubscribeContextRequest().setSubscriptionId(Constants.EXAMPLE_ID_NOT_FOUND);
		HttpURLConnection conn = buildPostUnsubscribeContextRequest("application/xml", reqDoc.toString());
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = UnsubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedID(Constants.EXAMPLE_ID_NOT_FOUND)
				.expectedNGSIStatusCode(470, "Subscription ID not found")
				.expectedErrorMessages("Context Subscription with ID \"" + Constants.EXAMPLE_ID_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a Subscription ID containing the string \"" + Constants.NOT_FOUND + "\" in the request.")
				.build();
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUnsubscribeContextNotWellFormedMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUnsubscribeContextRequest("application/xml", "foo");
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildBadRequestUnsubscribeContextResponseChecker(400, "Error parsing XML from payload:");
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUnsubscribeContextNotSchemaValidMustResultInBadRequest() throws Exception {
		UnsubscribeContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildIncompleteUnsubscribeContextRequestDocument();
		HttpURLConnection conn = buildPostUnsubscribeContextRequest("application/xml", reqDoc.toString());
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildBadRequestUnsubscribeContextResponseChecker(400, "The request body is not valid \"D=unsubscribeContextRequest\" according to the xsd schema definition.");
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	private HttpURLConnection buildPostUnsubscribeContextRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.POST
				.url(Constants.URL_UNSUBSCRIBE_CONTEXT)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

	private UnsubscribeContextResponseChecker buildOkUnsubscribeContextResponseChecker() {
		return UnsubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedID(Constants.EXAMPLE_ID)
				.expectedNGSIStatusCode(200, "Ok")
				.expectedMessages("Number of errors: 0")
				.build();
	}

	private UnsubscribeContextResponseChecker buildBadRequestUnsubscribeContextResponseChecker(int expectedHTTPStatusCode, String... expectedErrorMessages) {
		return UnsubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(expectedHTTPStatusCode)
				.expectedID(Constants.UNSUBSCRIBECONTEXTREQUEST_NO_SUBSCRIPTION_ID)
				.expectedNGSIStatusCode(400, "Bad request")
				.expectedErrorMessages(expectedErrorMessages)
				.build();
	}

}
