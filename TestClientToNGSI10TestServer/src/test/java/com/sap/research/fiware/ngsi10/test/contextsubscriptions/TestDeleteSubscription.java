package com.sap.research.fiware.ngsi10.test.contextsubscriptions;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.HttpURLConnection;

import noNamespace.StatusCode;
import noNamespace.UnsubscribeContextResponse;
import noNamespace.UnsubscribeContextResponseDocument;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.EntityEnclosingRequestWrapper;
import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UnsubscribeContextResponseChecker;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UnsubscribeContextResponseCheckerBuilder;

/*
 * The convenience function DELETE .../contextSubscriptions/{subscriptionID}
 * doubles the standard function POST .../unsubscribeContext, so both paths are
 * tested similarly.
 * @see com.sap.research.fiware.ngsi10.test.unsubscribecontext.TestUnsubscribeContext
 */
public class TestDeleteSubscription {

	/*
	 * Sending a DELETE request with entity using HttpUrlConnection fails due to
	 * a bug in the JDK. See: http://bugs.sun.com/view_bug.do?bug_id=7157360
	 * 
	 * So we have to use Apache HTTP client for this test.
	 * 
	 * We would like to use an HttpDelete and set a String entity on it, but
	 * this doesn't work, because HttpDelete is not a subclass of
	 * HttpEntityEnclosingRequestBase. So we have to use a workaround with
	 * EntityEnclosingRequestWrapper.
	 * 
	 * Unfortunately, Apache HTTP client sets a content-type header
	 * automatically, so we can't test with a request with entity set, but
	 * without content-type header. :-(
	 */
	@Test
	public void deleteOnContextSubscriptionIDWithPayloadMustResultInBadRequest() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(Constants.URL_EXAMPLE_SUBSCRIPTION_ID);
		EntityEnclosingRequestWrapper requestWrapper = new EntityEnclosingRequestWrapper(postRequest);
		requestWrapper.setMethod(HttpDelete.METHOD_NAME);
		requestWrapper.setEntity(new StringEntity("foo"));
		HttpResponse response = client.execute(requestWrapper);
		assertEquals(400, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream inputStream = entity.getContent();
			try {
				UnsubscribeContextResponseDocument unsubscribeContextResponseDocument = UnsubscribeContextResponseDocument.Factory.parse(inputStream);
				assertNotNull(unsubscribeContextResponseDocument);
				UnsubscribeContextResponse unsubscribeContextResponse = unsubscribeContextResponseDocument.getUnsubscribeContextResponse();
				assertNotNull(unsubscribeContextResponse);
				String subscriptionId = unsubscribeContextResponse.getSubscriptionId();
				assertNotNull(subscriptionId);
				assertEquals(Constants.EXAMPLE_ID, subscriptionId);
				StatusCode statusCode = unsubscribeContextResponse.getStatusCode();
				assertNotNull(statusCode);
				assertEquals(400, statusCode.getCode());
				assertEquals("Bad request", statusCode.getReasonPhrase());
				String statusCodeDetails = statusCode.getDetails().toString();
				assertTrue(statusCodeDetails.contains("This operation does not require a message body, yet there is one."));
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void deleteOnContextSubscriptionIDWithContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildDeleteSubscriptionIDRequest("text/plain", null);
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildBadRequestUnsubscribeContextResponseChecker("Superfluous media type in request. This request does not have a request body and thus does not need a media-type specification. Media Type of request: \"text/plain\".");
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void deleteOnContextSubscriptionIDNotFoundMustResultInNotFound() throws Exception {
		HttpURLConnection conn = buildDeleteSubscriptionIDNotFoundRequest(null, null);
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = UnsubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(404)
				.expectedID(Constants.EXAMPLE_ID_NOT_FOUND)
				.expectedNGSIStatusCode(470, "Subscription ID not found")
				.expectedErrorMessages("Context Subscription with ID \"" + Constants.EXAMPLE_ID_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a Subscription ID containing the string \"" + Constants.NOT_FOUND + "\" in the request.")
				.build();
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void deleteOnContextSubscriptionIDCorrectRequest() throws Exception {
		HttpURLConnection conn = buildDeleteSubscriptionIDRequest(null, null);
		UnsubscribeContextResponseChecker unsubscribeContextResponseChecker = buildOkUnsubscribeContextResponseChecker();
		unsubscribeContextResponseChecker.checkResponse(conn);
	}

	private HttpURLConnection buildDeleteSubscriptionIDRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.DELETE
				.url(Constants.URL_EXAMPLE_SUBSCRIPTION_ID)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

	private HttpURLConnection buildDeleteSubscriptionIDNotFoundRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.DELETE
				.url(Constants.URL_EXAMPLE_SUBSCRIPTION_ID_NOT_FOUND)
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

	private UnsubscribeContextResponseChecker buildBadRequestUnsubscribeContextResponseChecker(String... expectedErrorMessages) {
		return UnsubscribeContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(400)
				.expectedID(Constants.EXAMPLE_ID)
				.expectedNGSIStatusCode(400, "Bad request")
				.expectedErrorMessages(expectedErrorMessages)
				.build();
	}

}
