package com.sap.research.fiware.ngsi10.test.responsecheckers;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;

import noNamespace.StatusCode;

import org.apache.xmlbeans.XmlObject;

import com.sap.research.fiware.ngsi10.test.NgsiStatusCodes;

/**
 * Checks the response of a test.
 * 
 * RES is the NGSI type that we expect in the response (the one ending in
 * "Document"), or XmlObject if we don't expect a response.
 * 
 * */

public abstract class OllisAbstractResponseChecker<RES extends XmlObject> {

	/** The HTTP status code we expect to be returned from out test. */
	private int expectedHTTPStatusCode = -1;

	protected StatusFamily expectedStatusFamily = StatusFamily.UNSET;

	/** The NGSI status code we expect to be returned from out test. */
	protected NgsiStatusCodes expectedNGSIStatusCode = null;

	// don't care for error messages for now
	// protected List<String> expectedErrorMessages = null;

	// in fact, we don't care for any messages now :)
	// protected List<String> expectedMessages = null;

	/**
	 * The NGSI type we expect to be returned by the test, the name of which
	 * ends in "Document".
	 */
	protected RES responseDocument;

	/**
	 * Basically the NGSI type we expect to be returned by the test, BUT the
	 * name of which does NOT end in "Document".
	 */
	protected XmlObject response;

	/**
	 * The main status code included in the response (if such a thing exists :-/
	 * )
	 */
	protected StatusCode statusCode;

	public void setExpectedStatusCodes(int httpCode, NgsiStatusCodes ngsiCode) {
		setExpectedHTTPStatusCode(httpCode);
		setExpectedNGSIStatusCode(ngsiCode);
	} // end setExpectedStatusCodes()
	
	public void setExpectedHTTPStatusCode(int expectedHTTPStatusCode) {
		this.expectedHTTPStatusCode = expectedHTTPStatusCode;
		if (200 <= expectedHTTPStatusCode && expectedHTTPStatusCode < 300) {
			expectedStatusFamily = StatusFamily.OK;
		} else if (400 <= expectedHTTPStatusCode && expectedHTTPStatusCode < 500) {
			expectedStatusFamily = StatusFamily.ERROR;
		} else {
			throw new RuntimeException("Unknown expected HTTP status code: " + expectedHTTPStatusCode);
		}
	} // end setExpectedHTTPStatusCode()

	/**
	 * Set expected NGSI status code (and, implicitly, the NGSI reason phrase).
	 */
	public void setExpectedNGSIStatusCode(NgsiStatusCodes ngsiCode) {
		this.expectedNGSIStatusCode = ngsiCode;
	}

	public void checkResponse(HttpURLConnection conn) throws Exception {
		assertHTTPStatusCode(conn.getResponseCode());
		// conn.getResponseCode();
		readPayload(conn);
		assertNotNull(responseDocument);
		// System.out.println(responseDocument);
		extractResponse();
		assertNotNull(response);

		// get the "main" Status Code
		retrieveStatusCode();

		if (expectedNGSIStatusCode != null && statusCode == null) {
			// if we have not implemented the "retrieveStatusCode()" method (and
			// therefore statusCode is not set) BUT have set a
			// "expectedNGSIStatusCode", then we force failure.
			throw new RuntimeException("Internal error in UnitTests: you have set expectedNGSIStatusCode to a "
					+ "non-null value but have not provided the method retrieveStatusCode() in order to retrieve "
					+ "the actual StatusCode from the response.");
		}

		if (statusCode != null) {
			// check that expected and actual NGSI status codes match
			int ngsiStatusCode = statusCode.getCode();
			assertEquals(expectedNGSIStatusCode.getCode(), ngsiStatusCode);

			// check that expected and actual NGSI reason phrases match
			String ngsiReasonPhrase = statusCode.getReasonPhrase();
			assertEquals(expectedNGSIStatusCode.getReasonPhrase(), ngsiReasonPhrase);

			// TODO: check if statusCode details matches your expectation!
			// statusCode.getDetails();

		} // end if

		assertContent();
	} // end checkResponse()

	/**
	 * Implement this to set the "main" status code of the response to
	 * "stausCode", if there is one. Only if this method is implemented and
	 * actually sets "statusCode", then the expectedNGSIStatusCode will be
	 * tested.
	 * 
	 * When implementing this method ypu can expect that both responseDocument
	 * and response are set correctly (if there is a response expected, that is).
	 * 
	 */
	protected abstract void retrieveStatusCode();

	/** Implement this to set the content of the "response" object. */
	protected abstract void assertContent();

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

	/** Parses the inputStream and creates the xml beans Document. */
	protected void readPayloadFrom(InputStream inputStream) throws Exception {
		responseDocument = (RES) RES.Factory.parse(inputStream);
		assertNotNull(responseDocument);
	}

	/** This method returns the response. */
	public XmlObject getResponse() {
		return response;
	}
	public StatusCode getStatusCode() {
		return statusCode;
	}
	
	/** This method extracts the response, which can be retrieved by getResponse(). */
	protected void extractResponse() {

		// need a responseDocument to start the show
		if (responseDocument != null) {
			String docClassName = responseDocument.getClass().getSimpleName();
			// System.out.println("Document class name is " + docClassName);
			if (docClassName.endsWith(DOC_IMPL)) {
				int endIndex = docClassName.length() - DOC_IMPL.length();
				String className = docClassName.substring(0, endIndex);
				String methodNameGet = "get" + className;
				// System.out.println("will call: " + docClassName + "." + methodNameGet + "();");

				try {

					Method getMethod = responseDocument.getClass().getMethod(methodNameGet, (Class<?>[]) null);
					response = (XmlObject) getMethod.invoke(responseDocument, (Object[]) null);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} // end catch

				// XXX would be great to actually test if response is instance
				// of className
				try {
					// System.out.println("resDoc full name:" +
					// responseDocument.getClass().getName());
					Class responseClass = Class.forName("noNamespace.impl." + className + "Impl");
					responseClass.isAssignableFrom(response.getClass());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}

			} // end if

		} // end if
		assertNotNull(response);

	} // end extractResponse()

	protected enum StatusFamily {
		OK, ERROR, UNSET
	}

	public static final String DOC_IMPL = "DocumentImpl";

}
