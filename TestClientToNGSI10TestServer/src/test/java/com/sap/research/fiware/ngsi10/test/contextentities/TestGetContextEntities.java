package com.sap.research.fiware.ngsi10.test.contextentities;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.HttpURLConnection;

import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextElementResponse;
import noNamespace.ContextElementResponseDocument;
import noNamespace.StatusCode;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.NgsiStatusCodes;
import com.sap.research.fiware.ngsi10.test.responsecheckers.GetContextEntitiesChecker;

public class TestGetContextEntities {

	/*
	 * 
	 * B correct request => correct result
	 * 
	 * C correct request wirh scope parameter => correct result
	 * 
	 * D incorrect media type => error: unsupported media type
	 * 
	 * E request is missing required payload => error
	 * 
	 * F request that requires no payload has a content-type header => error
	 * 
	 * G request that requires no payload has a payload => error
	 * 
	 * H request with ".NOT_FOUND" => error 404 not found
	 * 
	 * I POST with ".EXISTING" => error 409 conflict
	 * 
	 * J XML not well-formed => error 400, bad request
	 * 
	 * K XML not valid against XSD schema => error 400, bad request
	 * 
	 * L request violates further constraints set forth in spec => error
	 * 
	 * M unallowed method => error 405, method not allowed
	 */

	/**
	 * GET /contextEntities/{EntityID}
	 * 
	 * B correct request => correct result
	 * 
	 * */
	@Test
	public void getOnContextEntityID_B() throws Exception {
		// no request document
		HttpURLConnection conn = buildGETConnection(Constants.URL_EXAMPLE_ENTITY_ID, Constants.CONTENT_TYPE_NONE, null);

		GetContextEntitiesChecker<ContextElementResponseDocument> checker = new GetContextEntitiesChecker<ContextElementResponseDocument>();

		checker.setExpectedStatusCodes(200, NgsiStatusCodes.Ok);

		checker.checkResponse(conn);
	}

	/**
	 * GET /contextEntities/{EntityID}
	 * 
	 * F request that requires no payload has a content-type header => error
	 * 
	 * */
	@Test
	public void getOnContextEntityID_F() throws Exception {
		HttpURLConnection conn = buildGETConnection(Constants.URL_EXAMPLE_ENTITY_ID, Constants.CONTENT_TYPE_XML, null);

		GetContextEntitiesChecker<ContextElementResponseDocument> checker = new GetContextEntitiesChecker<ContextElementResponseDocument>();

		checker.setExpectedStatusCodes(400, NgsiStatusCodes.BadRequest);

		checker.checkResponse(conn);
	}

	/**
	 * GET /contextEntities/{EntityID}
	 * 
	 * G request that requires no payload has a payload => error
	 * 
	 * */
	@Test
	public void getOnContextEntityID_G() throws Exception {

		// XXX: this test is very hard to implement since it is very hard to
		// send a payload in a GET request. since it is equally hard for anyone
		// wanting to build a decent server, we simply skip the test. :)

	}

	/**
	 * GET /contextEntities/{EntityID}
	 * 
	 * H request with ".NOT_FOUND" => error 404 not found
	 * 
	 * */
	@Test
	public void getOnContextEntityID_H() throws Exception {

		HttpURLConnection conn = buildGETConnection(Constants.URL_EXAMPLE_ENTITY_ID_NOT_FOUND,
				Constants.CONTENT_TYPE_NONE, null);

		GetContextEntitiesChecker<ContextElementResponseDocument> checker = new GetContextEntitiesChecker<ContextElementResponseDocument>();

		checker.setExpectedStatusCodes(404, NgsiStatusCodes.ContextElementNotFound);

		checker.checkResponse(conn);

	}


	/**
	 * Builds a GET HttpURLConnection with the given parameters.
	 * 
	 * @param url
	 *            The server URL
	 * @param contentType
	 *            The content type as in "application/xml", see Constants.java
	 * @param body
	 *            The body of the request, or null;
	 * 
	 * */
	private HttpURLConnection buildGETConnection(String url, String contentType, String body) throws Exception {
		return HttpRequestBuilder.GET.url(url).contentType(contentType).body(body).build();
	} // end buildGETConnection

	/**
	 * 
	 * IMPLEMENTATION BY "HAND", not using theResponseChecker.
	 * 
	 * GET /contextEntities/{EntityID}
	 * 
	 * F request that requires no payload has a content-type header => error
	 * 
	 * */
	@Test
	public void getOnContextEntityID_F_OLD() throws Exception {
		// no request document
		HttpURLConnection conn = buildGETConnection(Constants.URL_EXAMPLE_ENTITY_ID, Constants.CONTENT_TYPE_XML, null);
		assertEquals(400, conn.getResponseCode());
		InputStream inputStream;
		inputStream = conn.getErrorStream();
		try {
			ContextElementResponseDocument contextElementResponseDocument = ContextElementResponseDocument.Factory
					.parse(inputStream);
			assertNotNull(contextElementResponseDocument);
			ContextElementResponse contextElementResponse = contextElementResponseDocument.getContextElementResponse();
			assertNotNull(contextElementResponse);
			ContextElement contextElement = contextElementResponse.getContextElement();
			assertNotNull(contextElement);

			StatusCode statusCode = contextElementResponse.getStatusCode();
			assertNotNull(statusCode);

			int ngsiStatusCode = statusCode.getCode();
			assertEquals(400, ngsiStatusCode);
			statusCode.getDetails();
			// TODO: check if statusCode details matches your expectation!
			String ngsiReasonPhrase = statusCode.getReasonPhrase();
			assertEquals("Bad request", ngsiReasonPhrase);
		} finally {
			inputStream.close();
		}

	}

} // end class TestGetContextEntities
