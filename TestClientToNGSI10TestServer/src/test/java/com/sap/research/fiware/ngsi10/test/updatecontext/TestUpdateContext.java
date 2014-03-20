package com.sap.research.fiware.ngsi10.test.updatecontext;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import noNamespace.UpdateActionType;
import noNamespace.UpdateContextRequestDocument;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.NGSIRequestDocumentBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.SimpleContextElementCheckerBuilder;
import com.sap.research.fiware.ngsi10.test.responsecheckers.TopLevelElement;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UpdateContextResponseChecker;
import com.sap.research.fiware.ngsi10.test.responsecheckers.UpdateContextResponseCheckerBuilder;

public class TestUpdateContext {

	@Test
	public void postOnUpdateContextCompleteRequest() throws Exception {
		// entityId, contextAttributeName, contextAttributeValue
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125", "volume", "42dB"});
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:892652:57626583", "light", "2cd"});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.UPDATE);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseChecker updateContextResponseChecker = buildOkUpdateContextResponseChecker(requestedUpdates);
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextWithNoContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextRequest(null, null);
		UpdateContextResponseChecker updateContextResponseChecker = buildBadRequestUpdateContextResponseChecker(415, "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"[no media type information in request]\".");
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextWithWrongContentTypeMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextRequest("text/plain", null);
		UpdateContextResponseChecker updateContextResponseChecker = buildBadRequestUpdateContextResponseChecker(415, "media type does NOT EQUAL the required media type application/xml", "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \"application/xml\", Media Type of request: \"text/plain\".");
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextWithoutContentMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", null);
		UpdateContextResponseChecker updateContextResponseChecker = buildBadRequestUpdateContextResponseChecker(400, "This operation requires a message body, yet the payload is empty.");
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextEntityIdNotFoundActionUpdateMustResultInNotFound() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125" + "." + Constants.NOT_FOUND, "volume", "42dB"});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.UPDATE);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				// FIXME: the id is a property of the context element, so the NGSI status code should also belong to the element...
				.expectedMessages("Context element with ID \"" + requestedUpdates.get(0)[0] + "\" not found. *You* have provoked this error by specifying an element ID containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextEntityIdNotFoundActionAppendMustResultInOk() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125" + "." + Constants.NOT_FOUND, "volume", "42dB"});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.APPEND);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(200, "Ok")
				.expectedMessages("Number of errors: 0");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextEntityIdNotFoundActionDeleteMustResultInNotFound() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125" + "." + Constants.NOT_FOUND, "volume", "42dB"});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.DELETE);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedMessages("Context element with ID \"" + requestedUpdates.get(0)[0] + "\" not found. *You* have provoked this error by specifying an element ID containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextAttributeNotFoundActionUpdateMustResultInNotFound() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125", "volume" + "." + Constants.NOT_FOUND, "42dB"});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.UPDATE);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedMessages("Context attribute with name \"" + requestedUpdates.get(0)[1] + "\" not found. *You* have provoked this error by specifying an attribute name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextAttributeNotFoundActionAppendMustResultInOk() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125", "volume" + "." + Constants.NOT_FOUND, "42dB"});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.APPEND);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(200, "Ok")
				.expectedMessages("Number of errors: 0");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextAttributeNotFoundActionDeleteMustResultInNotFound() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125", "volume" + "." + Constants.NOT_FOUND, "42dB"});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.DELETE);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(404, "ContextElement not found")
				.expectedMessages("Context attribute with name \"" + requestedUpdates.get(0)[1] + "\" not found. *You* have provoked this error by specifying an attribute name containing the string \"" + Constants.NOT_FOUND + "\" in the request.");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}


	@Test
	public void postOnUpdateContextNotWellFormedMustResultInBadRequest() throws Exception {
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", "foo");
		UpdateContextResponseChecker updateContextResponseChecker = buildBadRequestUpdateContextResponseChecker(400, "Error parsing XML from payload:");
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextNotSchemaValidMustResultInBadRequest() throws Exception {
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildIncompleteUpdateContextRequestDocument();
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseChecker updateContextResponseChecker = buildBadRequestUpdateContextResponseChecker(400, "The request body is not valid \"D=updateContextRequest\" according to the xsd schema definition.");
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextEmptyContextValueActionUpdateMustResultInMissingParameter() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125", "volume", ""});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.UPDATE);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(471, "Missing parameter")
				.expectedMessages("The context value of attribute \"" + requestedUpdates.get(0)[1] + "\" must not be empty for update action \"" + UpdateActionType.UPDATE + "\".");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextEmptyContextValueActionAppendMustResultInMissingParameter() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125", "volume", ""});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.APPEND);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(471, "Missing parameter")
				.expectedMessages("The context value of attribute \"" + requestedUpdates.get(0)[1] + "\" must not be empty for update action \"" + UpdateActionType.APPEND + "\".");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	@Test
	public void postOnUpdateContextEmptyContextValueActionDeleteMustResultInOk() throws Exception {
		List<String[]> requestedUpdates = new ArrayList<String[]>();
		requestedUpdates.add(new String[] {"urn:x-oma-application:dpe:174521:62846125", "volume", ""});
		UpdateContextRequestDocument reqDoc = NGSIRequestDocumentBuilder.buildUpdateContextRequestDocument(requestedUpdates, UpdateActionType.DELETE);
		HttpURLConnection conn = buildPostUpdateContextRequest("application/xml", reqDoc.toString());
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(200, "Ok")
				.expectedMessages("Number of errors: 0");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		UpdateContextResponseChecker updateContextResponseChecker = updateContextResponseCheckerBuilder.build();
		updateContextResponseChecker.checkResponse(conn);
	}

	private HttpURLConnection buildPostUpdateContextRequest(String contentTypeHeader, String body) throws Exception {
		return HttpRequestBuilder.POST
				.url(Constants.URL_UPDATE_CONTEXT)
				.contentType(contentTypeHeader)
				.body(body)
				.build();
	}

	private UpdateContextResponseChecker buildOkUpdateContextResponseChecker(List<String[]> requestedUpdates) {
		UpdateContextResponseCheckerBuilder updateContextResponseCheckerBuilder = UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(200)
				.expectedTopLevelElement(TopLevelElement.CONTENT)
				.expectedNGSIStatusCode(200, "Ok")
				.expectedMessages("Number of errors: 0");
		for (String[] requestedUpdate : requestedUpdates) {
			SimpleContextElementCheckerBuilder simpleContextElementCheckerBuilder = SimpleContextElementCheckerBuilder.create()
					.expectedId(requestedUpdate[0])
					.expectedAttributeName(requestedUpdate[1])
					.expectedAttributeValue(requestedUpdate[2]);
			updateContextResponseCheckerBuilder.contextElementChecker(simpleContextElementCheckerBuilder.build());
		}
		return updateContextResponseCheckerBuilder.build();
	}

	private UpdateContextResponseChecker buildBadRequestUpdateContextResponseChecker(int expectedHTTPStatusCode, String... expectedErrorMessages) {
		return UpdateContextResponseCheckerBuilder.create()
				.expectedHTTPStatusCode(expectedHTTPStatusCode)
				.expectedTopLevelElement(TopLevelElement.ERROR)
				.expectedNGSIStatusCode(400, "Bad request")
				.expectedErrorMessages(expectedErrorMessages)
				.build();
	}

}
