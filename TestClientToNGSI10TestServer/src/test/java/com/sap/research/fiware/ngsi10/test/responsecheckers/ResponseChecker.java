package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import org.apache.xmlbeans.XmlObject;

public abstract class ResponseChecker {

	private int expectedHTTPStatusCode = -1;

	protected StatusFamily expectedStatusFamily = StatusFamily.UNSET;

	protected int expectedNGSIStatusCode = -1;

	protected String expectedNGSIReasonPhrase = null;

	/** This is the expected subscription id. */
	protected String expectedID = null;

	protected List<String> expectedErrorMessages = null;

	protected List<String> expectedMessages = null;

	protected XmlObject responseDocument;

	protected XmlObject response;

	void setExpectedHTTPStatusCode(int expectedHTTPStatusCode) {
		this.expectedHTTPStatusCode = expectedHTTPStatusCode;
		if (200 <= expectedHTTPStatusCode && expectedHTTPStatusCode < 300) {
			expectedStatusFamily = StatusFamily.OK;
		} else if (400 <= expectedHTTPStatusCode && expectedHTTPStatusCode < 500) {
			expectedStatusFamily = StatusFamily.ERROR;
		} else {
			throw new RuntimeException("Unknown expected HTTP status code: " + expectedHTTPStatusCode);
		}
	}

	void setExpectedID(String expectedID) {
		this.expectedID = expectedID;
	}

	void setExpectedNGSIStatusCode(int expectedNGSIStatusCode, String expectedNGSIReasonPhrase) {
		this.expectedNGSIStatusCode = expectedNGSIStatusCode;
		this.expectedNGSIReasonPhrase = expectedNGSIReasonPhrase;
	}

	void setExpectedErrorMessages(List<String> expectedErrorMessages) {
		this.expectedErrorMessages = expectedErrorMessages;
	}

	void setExpectedMessages(List<String> expectedMessages) {
		this.expectedMessages = expectedMessages;
	}

	public void checkResponse(HttpURLConnection conn) throws Exception {
		assertHTTPStatusCode(conn.getResponseCode());
		readPayload(conn);
		assertNotNull(responseDocument);
		extractResponse();
		assertNotNull(response);
		assertContent();
	}

	private void assertHTTPStatusCode(int actualHTTPStatusCode) {
		assertEquals(expectedHTTPStatusCode, actualHTTPStatusCode);
	}

	private void readPayload(HttpURLConnection conn) throws Exception {
		InputStream inputStream;
		switch (expectedStatusFamily) {
		case OK:
			inputStream = conn.getInputStream();
			break;
		case ERROR:
			inputStream = conn.getErrorStream();
			break;
		case UNSET:
		default:
			throw new RuntimeException("Status code family not set. This should never happen.");
		}
		try {
			readPayloadFrom(inputStream);
		} finally {
			inputStream.close();
		}
	}

	protected abstract void readPayloadFrom(InputStream inputStream) throws Exception;

	protected abstract void extractResponse();

	protected abstract void assertContent();

	protected enum StatusFamily {
		OK,
		ERROR,
		UNSET
	}

}
