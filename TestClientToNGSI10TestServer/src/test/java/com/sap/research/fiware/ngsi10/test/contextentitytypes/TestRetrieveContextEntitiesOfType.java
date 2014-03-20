package com.sap.research.fiware.ngsi10.test.contextentitytypes;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.HttpURLConnection;

import noNamespace.ContextElementResponseList;
import noNamespace.QueryContextResponse;
import noNamespace.QueryContextResponseDocument;
import noNamespace.StatusCode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.EntityEnclosingRequestWrapper;
import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.FullContextElementCheckerBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.QueryContextResponseChecker;
import com.sap.research.fiware.ngsi10.test.responsecheckers.QueryContextResponseCheckerBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.TopLevelElement;

/*
 * The resources /contextEntityTypes/{typeName} and
 * /contextEntityTypes/{typeName}/attributes are specified to behave equally,
 * so every test is performed for both paths here.
 */
public class TestRetrieveContextEntitiesOfType {

	@Test
	public void getOnContextEntityTypesCorrectRequest() throws Exception {
		getCorrectRequest(Constants.URL_EXAMPLE_TYPE_NAME, Constants.DEFAULT_ATTRIBUTE_NAME, Constants.DEFAULT_ATTRIBUTE_DOMAIN_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributesCorrectRequest() throws Exception {
		getCorrectRequest(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES, Constants.DEFAULT_ATTRIBUTE_NAME, Constants.DEFAULT_ATTRIBUTE_DOMAIN_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributesAttributeNameCorrectRequest() throws Exception {
		getCorrectRequest(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME, Constants.EXAMPLE_ATTRIBUTE_NAME, Constants.DEFAULT_ATTRIBUTE_DOMAIN_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributeDomainsAttributeDomainNameCorrectRequest() throws Exception {
		getCorrectRequest(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME, Constants.DEFAULT_ATTRIBUTE_NAME, Constants.EXAMPLE_ATTRIBUTE_DOMAIN_NAME);
	}

	private void getCorrectRequest(String url, String expectedAttributeName, String expectedAttributeDomainName) throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(url, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildOkQueryContextResponseChecker(expectedAttributeName, expectedAttributeDomainName);
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getOnContextEntityTypesWithContentTypeMustResultInBadRequest() throws Exception {
		getWithContentType(Constants.URL_EXAMPLE_TYPE_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributesWithContentTypeMustResultInBadRequest() throws Exception {
		getWithContentType(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES);
	}

	@Test
	public void getOnContextEntityTypeAttributesAttributeNameWithContentTypeMustResultInBadRequest() throws Exception {
		getWithContentType(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributeDomainsAttributeDomainNameWithContentTypeMustResultInBadRequest() throws Exception {
		getWithContentType(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME);
	}

	private void getWithContentType(String url) throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(url, "text/plain", null);
		QueryContextResponseChecker queryContextResponseChecker = buildBadRequestQueryContextResponseChecker("Superfluous media type in request. This request does not have a request body and thus does not need a media-type specification. Media Type of request: \"text/plain\".");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getOnContextEntityTypesWithPayloadMustResultInBadRequest() throws Exception {
		getWithPayload(Constants.URL_EXAMPLE_TYPE_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributesWithPayloadMustResultInBadRequest() throws Exception {
		getWithPayload(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES);
	}

	@Test
	public void getOnContextEntityTypeAttributesAttributeNameWithPayloadMustResultInBadRequest() throws Exception {
		getWithPayload(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributeDomainsAttributeDomainNameWithPayloadMustResultInBadRequest() throws Exception {
		getWithPayload(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME);
	}

	/*
	 * Sending a GET request with entity using HttpUrlConnection fails, because
	 * calling setDoOutput(true) on it switches the method from GET to POST.
	 * 
	 * So we have to use Apache HTTP client for this test.
	 * 
	 * We would like to use an HttpGet and set a String entity on it, but this
	 * doesn't work, because HttpGet is not a subclass of
	 * HttpEntityEnclosingRequestBase. So we have to use a workaround with
	 * EntityEnclosingRequestWrapper.
	 * 
	 * Unfortunately, Apache HTTP client sets a content-type header
	 * automatically, so we can't test with a request with entity set, but
	 * without content-type header. :-(
	 */
	private void getWithPayload(String url) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		EntityEnclosingRequestWrapper requestWrapper = new EntityEnclosingRequestWrapper(postRequest);
		requestWrapper.setMethod(HttpGet.METHOD_NAME);
		requestWrapper.setEntity(new StringEntity("foo"));
		HttpResponse response = client.execute(requestWrapper);
		assertEquals(400, response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream inputStream = entity.getContent();
			try {
				QueryContextResponseDocument queryContextResponseDocument = QueryContextResponseDocument.Factory.parse(inputStream);
				assertNotNull(queryContextResponseDocument);
				QueryContextResponse queryContextResponse = queryContextResponseDocument.getQueryContextResponse();
				assertNotNull(queryContextResponse);
				ContextElementResponseList contextElementResponseList = queryContextResponse.getContextResponseList();
				assertNull(contextElementResponseList);
				StatusCode statusCode = queryContextResponse.getErrorCode();
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
	public void getOnContextEntityTypesWithInvalidTypeNameMustResultInBadRequest() throws Exception {
		getWithInvalidTypeName(Constants.URL_EXAMPLE_TYPE_NAME_INVALID);
	}

	@Test
	public void getOnContextEntityTypeAttributesWithInvalidTypeNameMustResultInBadRequest() throws Exception {
		getWithInvalidTypeName(Constants.URL_EXAMPLE_TYPE_NAME_INVALID_ATTRIBUTES);
	}

	@Test
	public void getOnContextEntityTypeAttributesAttributeNameWithInvalidTypeNameMustResultInBadRequest() throws Exception {
		getWithInvalidTypeName(Constants.URL_EXAMPLE_TYPE_NAME_INVALID_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME);
	}

	@Test
	public void getOnContextEntityTypeAttributeDomainsAttributeDomainNameWithInvalidTypeNameMustResultInBadRequest() throws Exception {
		getWithInvalidTypeName(Constants.URL_EXAMPLE_TYPE_NAME_INVALID_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME);
	}

	private void getWithInvalidTypeName(String url) throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(url, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildBadRequestQueryContextResponseChecker("typeName '" + Constants.EXAMPLE_TYPE_NAME_INVALID + "' is not a valid URI: Illegal character in opaque part at index 5:");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getTypeNameNotFoundMustResultInNotFound() throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(Constants.URL_EXAMPLE_TYPE_NAME_NOT_FOUND, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildNotFoundQueryContextResponseChecker("Context entity type with type name \"" + Constants.EXAMPLE_TYPE_NAME_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a type name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getTypeNameNotFoundAttributesMustResultInNotFound() throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(Constants.URL_EXAMPLE_TYPE_NAME_NOT_FOUND_ATTRIBUTES, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildNotFoundQueryContextResponseChecker("Context entity type with type name \"" + Constants.EXAMPLE_TYPE_NAME_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a type name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getTypeNameNotFoundAttributesExampleAttributeNameMustResultInNotFound() throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(Constants.URL_EXAMPLE_TYPE_NAME_NOT_FOUND_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildNotFoundQueryContextResponseChecker("Context entity type with type name \"" + Constants.EXAMPLE_TYPE_NAME_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a type name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getTypeNameAttributesExampleAttributeNameNotFoundMustResultInNotFound() throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME_NOT_FOUND, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildNotFoundQueryContextResponseChecker("Attribute with name \"" + Constants.EXAMPLE_ATTRIBUTE_NAME_NOT_FOUND +"\" not found. *You* have provoked this error by specifying an attribute name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getTypeNameNotFoundAttributeDomainsExampleAttributeDomainNameMustResultInNotFound() throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(Constants.URL_EXAMPLE_TYPE_NAME_NOT_FOUND_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildNotFoundQueryContextResponseChecker("Context entity type with type name \"" + Constants.EXAMPLE_TYPE_NAME_NOT_FOUND + "\" not found. *You* have provoked this error by specifying a type name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void getTypeNameAttributeDomainsExampleAttributeDomainNameNotFoundMustResultInNotFound() throws Exception {
		HttpURLConnection conn = buildGetContextEntityTypesRequest(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME_NOT_FOUND, null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildNotFoundQueryContextResponseChecker("Attribute domain with name \"" + Constants.EXAMPLE_ATTRIBUTE_DOMAIN_NAME_NOT_FOUND +"\" not found. *You* have provoked this error by specifying an attribute domain name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		queryContextResponseChecker.checkResponse(conn);
	}

	private QueryContextResponseChecker buildBadRequestQueryContextResponseChecker(String... expectedErrorMessages) {
		return QueryContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(400)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(400, "Bad request")
				.expectedErrorMessages(expectedErrorMessages)
				.build();
	}

	private QueryContextResponseChecker buildNotFoundQueryContextResponseChecker(String... expectedErrorMessages) {
		QueryContextResponseChecker queryContextResponseChecker = QueryContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(404)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedErrorMessages(expectedErrorMessages)
				.build();
		return queryContextResponseChecker;
	}

	private QueryContextResponseChecker buildOkQueryContextResponseChecker(String expectedAttributeName, String expectedAttributeDomainName) {
		return QueryContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.contextElementChecker(FullContextElementCheckerBuilder.create()
						.expectedId(Constants.DEFAULT_ID)
						.expectedTypeName(Constants.EXAMPLE_TYPE_NAME)
						.expectedAttributeName(expectedAttributeName)
						.expectedAttributeDomainName(expectedAttributeDomainName)
						.build())
				.expectedNGSIStatusCode(200, "Ok")
				.expectedMessages("Number of errors: 0")
				.build();
	}

	private HttpURLConnection buildGetContextEntityTypesRequest(String url, String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.GET
				.url(url)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

}
