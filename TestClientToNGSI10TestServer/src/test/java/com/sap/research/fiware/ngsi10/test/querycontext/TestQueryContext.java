package com.sap.research.fiware.ngsi10.test.querycontext;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import noNamespace.QueryContextRequestDocument;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.NGSIRequestDocumentBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.FullContextElementCheckerBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.NotFoundContextElementCheckerBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.QueryContextResponseChecker;
import com.sap.research.fiware.ngsi10.test.responsecheckers.QueryContextResponseCheckerBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.TopLevelElement;

public class TestQueryContext {

	@Test
	public void postOnQueryContextCompleteRequest() throws Exception {
		List<String> reqestedEntityIds = new ArrayList<String>();
		reqestedEntityIds.add("urn:x-oma-application:dpe:927652:98759123");
		reqestedEntityIds.add("urn:x-oma-application:dpe:873223:23856129");
		List<String> requestedAttributes = new ArrayList<String>();
		requestedAttributes.add("light");
		requestedAttributes.add("volume");
		QueryContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteQueryContextRequestDocument(reqestedEntityIds, requestedAttributes);
		HttpURLConnection conn = buildPostQueryContextRequest("application/xml", reqDoc.toString());
		QueryContextResponseChecker queryContextResponseChecker = buildOkQueryContextResponseChecker(reqestedEntityIds, requestedAttributes);
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnQueryContextWithNoContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostQueryContextRequest(null, null);
		QueryContextResponseChecker queryContextResponseChecker = buildBadRequestQueryContextResponseChecker(415, "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"[no media type information in request]\".");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnQueryContextWithWrongContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostQueryContextRequest("text/plain", null);
		QueryContextResponseChecker queryContextResponseChecker = buildBadRequestQueryContextResponseChecker(415, "media type does NOT EQUAL the required media type application/xml", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"text/plain\".");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnQueryContextWithoutContentMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostQueryContextRequest("application/xml", null);
		QueryContextResponseChecker queryContextResponseChecker = buildBadRequestQueryContextResponseChecker(400, "This operation requires a message body, yet the payload is empty.");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnQueryContextEntityIdNotFoundMustResultInNotFound() throws Exception {
		String requestedEntityId = "urn:x-oma-application:dpe:927652:98759123" + "." + Constants.NOT_FOUND;
		List<String> reqestedEntityIds = new ArrayList<String>();
		reqestedEntityIds.add(requestedEntityId);
		List<String> requestedAttributes = new ArrayList<String>();
		requestedAttributes.add("light");
		QueryContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteQueryContextRequestDocument(reqestedEntityIds, requestedAttributes);
		HttpURLConnection conn = buildPostQueryContextRequest("application/xml", reqDoc.toString());
		QueryContextResponseChecker queryContextResponseChecker = buildNotFoundQueryContextResponseChecker(requestedEntityId);
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnQueryContextAttributeNotFoundMustResultInNotFound() throws Exception {
		String requestedEntityId = "urn:x-oma-application:dpe:927652:98759123";
		List<String> reqestedEntityIds = new ArrayList<String>();
		reqestedEntityIds.add(requestedEntityId);
		List<String> requestedAttributes = new ArrayList<String>();
		String requestedAttribute = "light" + "." + Constants.NOT_FOUND;
		requestedAttributes.add(requestedAttribute);
		QueryContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildCompleteQueryContextRequestDocument(reqestedEntityIds, requestedAttributes);
		HttpURLConnection conn = buildPostQueryContextRequest("application/xml", reqDoc.toString());
		QueryContextResponseChecker queryContextResponseChecker = buildAttributeNotFoundQueryContextResponseChecker(requestedEntityId, requestedAttribute);
		queryContextResponseChecker.checkResponse(conn);
	}

	private QueryContextResponseChecker buildAttributeNotFoundQueryContextResponseChecker(String requestedEntityId, String requestedAttribute) {
		QueryContextResponseCheckerBuilder responseCheckerBuilder = QueryContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedMessages("Context entity with attribute \"" + requestedAttribute + "\" not found. *You* have provoked this error by specifying an attribute containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		responseCheckerBuilder.contextElementChecker(NotFoundContextElementCheckerBuilder.create()
				.expectedId(requestedEntityId)
				.expectedTypeName(Constants.EXAMPLE_TYPE_NAME)
				.expectedAttribute(requestedAttribute).build());
		return responseCheckerBuilder.build();
	}

	@Test
	public void postOnQueryContextNotWellFormedMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostQueryContextRequest("application/xml", "foo");
		QueryContextResponseChecker queryContextResponseChecker = buildBadRequestQueryContextResponseChecker(400, "Error parsing XML from payload:");
		queryContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnQueryContextNotSchemaValidMustResultInBadRequest() throws Exception {
		QueryContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildIncompleteQueryContextRequestDocument();
		HttpURLConnection conn = buildPostQueryContextRequest("application/xml", reqDoc.toString());
		QueryContextResponseChecker queryContextResponseChecker = buildBadRequestQueryContextResponseChecker(400, "The request body is not valid \"D=queryContextRequest\" according to the xsd schema definition.");
		queryContextResponseChecker.checkResponse(conn);
	}

	private HttpURLConnection buildPostQueryContextRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.POST
				.url(Constants.URL_QUERY_CONTEXT)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

	private QueryContextResponseChecker buildNotFoundQueryContextResponseChecker(String requestedEntityId) {
		QueryContextResponseCheckerBuilder responseCheckerBuilder = QueryContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedMessages("Context entity with ID \"" + requestedEntityId + "\" not found. *You* have provoked this error by specifying a entity ID containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		responseCheckerBuilder.contextElementChecker(NotFoundContextElementCheckerBuilder.create()
				.expectedId(requestedEntityId)
				.expectedTypeName(Constants.EXAMPLE_TYPE_NAME).build());
		return responseCheckerBuilder.build();
	}

	private QueryContextResponseChecker buildOkQueryContextResponseChecker(List<String> requestedEntityIds, List<String> requestedAttributes) {
		QueryContextResponseCheckerBuilder responseCheckerBuilder = QueryContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(200, "Ok")
				.expectedMessages("Number of errors: 0");
		for (String reqestedEntityId : requestedEntityIds) {
			FullContextElementCheckerBuilder fullContextElementCheckerBuilder = FullContextElementCheckerBuilder.create()
					.expectedId(reqestedEntityId)
					.expectedTypeName(Constants.EXAMPLE_TYPE_NAME)
					.expectedAttributeDomainName(Constants.DEFAULT_ATTRIBUTE_DOMAIN_NAME);
			for (String requestedAttribute : requestedAttributes) {
				fullContextElementCheckerBuilder.expectedAttributeName(requestedAttribute);
			}
			responseCheckerBuilder.contextElementChecker(fullContextElementCheckerBuilder.build());
		}
		return responseCheckerBuilder.build();
	}

	private QueryContextResponseChecker buildBadRequestQueryContextResponseChecker(int expectedHTTPStatusCode, String... expectedErrorMessages) {
		return QueryContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(expectedHTTPStatusCode)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(400, "Bad request")
				.expectedErrorMessages(expectedErrorMessages)
				.build();
	}

}
