package com.sap.research.fiware.ngsi10.test.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

public class RequestTestHelper<REQ extends XmlObject /* , RES extends XmlObject */> {

	private Logger logger = Logger.getLogger(this.getClass());

	// parameters from the request
	private HttpHeaders headers;
	private UriInfo uri;
	private Request request;
	private String payload;

	// requirements, to be set by the user of this class
	private MediaType requiredMediaType = MediaType.APPLICATION_XML_TYPE;
	private boolean requiresPayload = false;
	private boolean allowsQueryParams = false;

	// the expected XML request object, if required and if it can be parsed
	// correctly, otherwise null
	private REQ doc = null;

	// Used for XML validation
	// Create an XmlOptions instance and set the error listener.
	ArrayList<XmlError> xmlParseErrorList = new ArrayList<XmlError>();
	XmlOptions xmlValidateOptions = new XmlOptions().setErrorListener(xmlParseErrorList);

	// keeps track of all issues (errors and warnings) in the request
	private IssueRecords issues = new IssueRecords();

	@SuppressWarnings("unused")
	private RequestTestHelper() {
	}

	public RequestTestHelper(Request request, UriInfo uri, HttpHeaders headers, String payload) {
		this.request = request;
		this.uri = uri;
		this.headers = headers;
		this.payload = payload;
	} // end constructor

	/*
	 * Setters
	 */

	/**
	 * Set the required media type of the request. If set to null, the request
	 * must not have a media type (and there will be an error in case there is).
	 */
	public void setRequiredMediaType(MediaType mediaType) {
		this.requiredMediaType = mediaType;

	} // end setRequiredMediaType()

	/** Sets if the request requires a payload. */
	public void setRequiresPayload(boolean requiresPayload) {
		this.requiresPayload = requiresPayload;
	} // setRequiresPayload()

	/** Sets if the request requires query parameters. */
	public void setAllowsQueryParams(boolean requiresParams) {
		this.allowsQueryParams = requiresParams;
	}

	/*
	 * Getters
	 */

	/** Returns true if is neither null nor empty. */
	public boolean hasPayload() {
		return ((null != payload) && (!payload.isEmpty()));
	}

	/** Returns true if this Request requires a payload. */
	public boolean requiresPayload() {
		return this.requiresPayload;
	}

	public MediaType getRequiredMediaType() {
		return this.requiredMediaType;
	}

	/**
	 * Returns if this operation requires query parameters, as set by
	 * setRequiresQueryParams().
	 */
	public boolean allowsQueryParams() {
		return this.allowsQueryParams;
	}

	/**
	 * @return the parsed Request Document
	 */
	public REQ getReqDoc() {
		return doc;
	}

	/*
	 * Warnings & Errors
	 */

	// delegate to issues
	public boolean hasErrors() {

		return issues.hasErrors();
	}

	public IssueRecord getFirstError() {
		return issues.getFirstError();
	}

	public void addNewIssueRecord(Status httpStatus, String desc, NgsiStatusCodes ngsiStatusCode) {
		issues.addNewIssueRecord(httpStatus, desc, ngsiStatusCode);
	} // end addNewIssueRecord()

	/** Returns if this request has query parameters. */
	public boolean hasQueryParameters() {

		if ((uri != null) && (uri.getQueryParameters() != null) && (uri.getQueryParameters().keySet() != null)
				&& (!uri.getQueryParameters().keySet().isEmpty())) {
			return true;
		}
		return false;
	}

	/** Returns the (keys of the) query parameters included in this request. */
	public Set<String> getQueryParameterKeys() {

		if (hasQueryParameters()) {
			return uri.getQueryParameters().keySet();
		}
		return null;
	} // getQueryParameterKeys()

	/**
	 * Parses and validates the request payload to be of type REQ and writes the
	 * request to internal variable doc (of type REQ); this can be retrieved
	 * with @see getReqDoc. document is null if the payload does not contain
	 * valid XML or the payload cannot be validated against the XSD schema.
	 * 
	 * checks: column J: "400/400" -- xml request not well-formed column K:
	 * "400/400" -- xml request not valid against schema
	 */
	private void parseReqest() {

		if (!hasPayload()) {
			return;
		}

		StringBuilder errStr = new StringBuilder();
		try {
			doc = (REQ) REQ.Factory.parse(payload);
		} catch (XmlException e) {

			// column J: "400/400" -- xml request not well-formed
			errStr.append("Error parsing XML from payload:\n");
			errStr.append(payload);
			errStr.append("\n");
			errStr.append("Error message is \"");
			errStr.append(e.getMessage());
			errStr.append("\"\n");

			addNewIssueRecord(Status.BAD_REQUEST, errStr.toString(), NgsiStatusCodes.BadRequest);
			return;
		}

		// Validate the XML based on the schema definition.
		xmlParseErrorList.clear();
		boolean isValid = doc.validate(xmlValidateOptions);

		// If the XML isn't valid, loop through the listener's contents,
		// printing contained messages.
		if (!isValid) {

			// column K: "400/400" -- xml request not valid against schema

			// not valid according to XSD schema definition
			errStr.append("The request body is not valid \"" + doc.schemaType()
					+ "\" according to the xsd schema definition.\n");
			for (XmlError error : xmlParseErrorList) {
				errStr.append("Validation error : " + error.getMessage() + ". ");
				errStr.append("Location of invalid XML: " + error.getCursorLocation().xmlText() + "\n");
			}

			addNewIssueRecord(Status.BAD_REQUEST, errStr.toString(), NgsiStatusCodes.BadRequest);

		}

	} // end isValidReqest()

	/**
	 * Checks for generic errors and fills the list of issues (i.e., a List of
	 * IssuRecord). This includes parsing the request to a REQ.
	 * 
	 * 
	 * 
	 * Checks the following errors (column numbers relate to the excel file in
	 * the /doc folder):
	 * 
	 * <ol>
	 * <li>column ?: "400 bad request/ 472 Invalid parameter" -- parameters only
	 * allowed in GET (for scoping)
	 * 
	 * <li>column D: "415 unsupported media type/ 400 Bad request" --
	 * unsupported media type
	 * 
	 * <li>column E: "400/400" -- payload missing
	 * 
	 * <li>column F: "400/400" -- superfluous media-type header where no payload
	 * required
	 * 
	 * <li>column G: "400/400" -- superfluous payload where there is none
	 * expected (e.g., in GET)
	 * 
	 * <li>column J: "400/400" -- xml request not well-formed
	 * 
	 * <li>column K: "400/400" -- xml request not valid against schema
	 * reqHlp.parseReqest();
	 * 
	 * </ol>
	 * 
	 * */

	public void checkRequest() {

		// column ?: "400 bad request/ 472 Invalid parameter" -- parameters only
		// allowed in GET (for scoping)

		if (!allowsQueryParams() && hasQueryParameters()) {

			String desc = "This request does allow query parameters for scoping. Query parameters found :\n"
					+ queryParametersToString("    ");
			addNewIssueRecord(Status.BAD_REQUEST, desc, NgsiStatusCodes.InvalidParameter);
		}

		// column F: "400/400" -- superfluous media-type header where no payload
		// required
		if (null == getRequiredMediaType() && !matchesRequiredMediaType()) {

			String desc = "Superfluous media type in request. This request does not have a request body and thus "
					+ "does not need a media-type specification. Media Type of request: \"" + getMediaType() + "\".";
			addNewIssueRecord(Status.BAD_REQUEST, desc, NgsiStatusCodes.BadRequest);
		}

		// column D: "415 unsupported media type/ 400 Bad request" --
		// unsupported media type
		if (null != getRequiredMediaType() && !matchesRequiredMediaType()) {

			String desc = "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \""
					+ getRequiredMediaType() + "\", Media Type of request: \"" + getMediaType() + "\".";
			addNewIssueRecord(Status.UNSUPPORTED_MEDIA_TYPE, desc, NgsiStatusCodes.BadRequest);
		}

		// column E: "400/400" -- payload missing
		if (requiresPayload() && !hasPayload()) {
			String desc = "This operation requires a message body, yet the payload is empty.\n";
			addNewIssueRecord(Status.BAD_REQUEST, desc, NgsiStatusCodes.BadRequest);

		}

		// column G: "400/400" -- superfluous payload where there is none
		// expected (e.g., in GET)
		if (!requiresPayload() && hasPayload()) {
			String desc = "This operation does not require a message body, yet there is one.\n";
			addNewIssueRecord(Status.BAD_REQUEST, desc, NgsiStatusCodes.BadRequest);

		}

		parseReqest();

	} // end checkForErrors()

	/**
	 * Generate a verbose analysis of the request to be used in the details
	 * field of the NGSI StatusCode.
	 */

	public String getRequestInformation() {

		StringBuilder buf = new StringBuilder();

		buf.append("\n\n===============================================================\n");

		// ***
		// WARNINGS & ERRORS first
		// ***
		buf.append("\n*** Errors and Warnings ***\n");

		buf.append("  Number of errors: " + issues.getIssues().size());
		buf.append("\n");

		for (IssueRecord iss : issues.getIssues()) {
			buf.append("    ");
			buf.append(iss.toString());
			buf.append("\n");
		}
		buf.append("\n");

		buf.append("*** Request details ***\n");
		// GET
		// http://localhost:8080/NGSIRestInterface/NGSI10/contextEntities/room3
		buf.append("  ");
		if (null == request) {
			buf.append("null\n");
		} else {
			buf.append(request == null ? "" : request.getMethod());
			buf.append(" ");
			buf.append(uri == null ? "" : uri.getRequestUri());
			buf.append("\n");
		}
		// ***
		// URI INFO
		// ***
		if (null == uri) {
			buf.append("  null\n");
		} else {
			buf.append("  Absolute path: ");
			buf.append(uri.getAbsolutePath());
			buf.append("\n");
			buf.append("  Base Uri: ");
			buf.append(uri.getBaseUri());
			buf.append("\n");

			buf.append("  Path segments:\n");
			int i = 1;
			for (PathSegment seg : uri.getPathSegments()) {

				buf.append("    segment ");
				buf.append(i++);
				buf.append(" : ");
				buf.append(seg.getPath());
				buf.append("\n");
			}

			buf.append("  Query parameters: \n");
			if (uri.getQueryParameters().keySet() == null) {
				buf.append("    QueryParameters == null\n");
			} else {
				// we have query parameters

				String params = queryParametersToString("    ");
				buf.append(params);
				buf.append("\n");

				// if (!allowsQueryParams() && hasQueryParameters()) {
				// addOutputWarning("Request does not require query parameters yet has the following parameters:\n"
				// + params);
				// }

			} // end else
		}
		// ***
		// HEADERS
		// ***
		buf.append("*** Headers ***\n");

		if (null == headers) {
			buf.append("  Headers is null\n");
			// addOutputWarning("The request has no headers (headers == null).");
			// // ERROR or WARNING?
		} else {

			//
			// Request Headers
			//

			// Request Headers
			buf.append("  Request headers\n");
			MultivaluedMap<String, String> mvm = headers.getRequestHeaders();
			Set<String> keys = mvm.keySet();
			for (String key : keys) {
				buf.append("    header \"");
				buf.append(key);
				buf.append("\"\n");
				List<String> strList = mvm.get(key);
				for (String str : strList) {
					buf.append("      ");
					buf.append(str);
					buf.append("\n");
				} // end for
			} // end for

			//
			// Media Type
			//

			buf.append("  MediaType");
			if (null == headers.getMediaType()) {
				buf.append(" is null\n");
				// // ERROR
				// String error =
				// "Media Type of the request is null. Did you send a header \"Content-Type: application/xml\" in the request? Expected Media Type is \""
				// + getRequiredMediaType() + "\"";
				// addOutputError(error);

			} else {
				buf.append("(type/subtype): ");
				buf.append(getMediaType());
				buf.append("\n");

				// does MediaType match "application/xml"?
				if (matchesRequiredMediaType()) {
					// yes: GOOD
					buf.append("      media type EQUALS ");
					buf.append(requiredMediaType);
					// buf.append(" (good)\n");
				} else {
					// no
					buf.append("      media type does NOT EQUAL the required media type ");
					buf.append(requiredMediaType);
					// buf.append(" (bummer!)\n");
					// // ERROR
					// String error =
					// "Unsupported Media Type. Currently only \"application/xml\" is supported. Expected: \""
					// + getRequiredMediaType() +
					// "\", Media Type of request: \"" + getMediaType() + "\".";
					// addOutputError(error);
				} // end else
				buf.append("\n");
			} // end else

			buf.append("  Acceptable languages\n");

			Iterator<Locale> it = headers.getAcceptableLanguages().iterator();
			if (!it.hasNext()) {
				buf.append("    empty");
			}
			while (it.hasNext()) {
				Locale loc = it.next();
				buf.append(loc.getDisplayCountry());
				if (it.hasNext()) {
					buf.append(", ");
				} // end if
			} // end while
			buf.append("\n");

		} // end else if( headers == null)

		// ***
		// PAYLOAD
		// ***
		buf.append("*** Payload ***\n");
		if (null == payload) {
			buf.append("  payload is null.\n");
			// if (requiresPayload) {
			// addOutputError("This operation requires a message body, yet the payload is null. This may be an indication for a client as well as a server error.\n");
			// }
		} else if (payload.isEmpty()) {
			buf.append("  payload is empty (i.e., lenght = 0).\n");
			// if (requiresPayload) {
			// addOutputError("This operation requires a message body, yet the payload is empty.\n");
			// }
		} else {
			// there is a payload
			// if (!requiresPayload) {
			// addOutputWarning("This operation does not require a message body, yet there is one.\n");
			// }
			buf.append("  payload start >>>\n");
			buf.append(payload);
			buf.append("  <<< payload end\n");
		}

		buf.append("===============================================================\n\n");

		// logger.info(buf.toString());
		return buf.toString();
	}// end getRequestInformation()

	String queryParametersToString() {
		return queryParametersToString("");
	}

	String queryParametersToString(String indent) {
		if (null == indent) {
			indent = "";
		}
		StringBuffer buf = new StringBuffer();

		if (!hasQueryParameters()) {
			buf.append(indent);
			buf.append("This request has no QueryParameters.\n");
		} else {
			int j = 0;
			for (String key : uri.getQueryParameters().keySet()) {
				// buf.append("    key ");
				// buf.append(j++);
				// buf.append(": ");
				buf.append(indent);
				buf.append(key);
				buf.append("=");

				Iterator<String> it = uri.getQueryParameters().get(key).iterator();
				while (it.hasNext()) {
					String val = it.next();
					buf.append(val);
					if (it.hasNext()) {
						buf.append(", ");
					}
				} // end while

				/*
				 * for(String val : uri.getQueryParameters().get(key)) {
				 * buf.append(val); buf.append(" "); }
				 */

				buf.append("\n");
			} // end for
		} // end else

		return buf.toString();

	} // end queryParametersToString()

	/**
	 * Returns true if type and subtype equals the required media types,
	 * ignoring case and stuff like encoding.
	 * */
	public boolean matchesRequiredMediaType() {

		MediaType suppliedMediaType = (headers == null ? null : headers.getMediaType());

		if (requiredMediaType == null) {
			if (suppliedMediaType == null) {
				// both are null, that's fine
				return true;
			} else {
				// supplied media type is not-null => no match
				return false;
			}
		} // end if

		if (suppliedMediaType == null) {
			// there are no headers at all or no content-type header...
			// ... that's NOT OK since requiredMediaType != null
			return false;
		} // end if

		boolean matchType = requiredMediaType.getType().equalsIgnoreCase(suppliedMediaType.getType());
		boolean matchSubtype = requiredMediaType.getSubtype().equalsIgnoreCase(suppliedMediaType.getSubtype());

		return matchType && matchSubtype;

	} // end matchesRequiredMediaType()

	/**
	 * Returns the media type and subtype of the request in lowercase in the
	 * following fashion: "application/xml", or null if no media type is set.
	 */
	public String getMediaType() {

		if (headers == null) {
			return null;
		}

		if (headers.getMediaType() == null) {
			return "[no media type information in request]";
		}

		StringBuilder b = new StringBuilder();
		b.append(headers.getMediaType().getType());
		b.append("/");
		b.append(headers.getMediaType().getSubtype());

		return b.toString().toLowerCase();
	} // end getMediaType()

	
	
	/**
	 * Checks if entityId contains the String Constants.NOT_FOUND and if so,
	 * adds an issue to the request helper object stating that the context
	 * element identified by entityId could not be found.
	 */
	public void checkForNotFoundError(String entityId) {

		if (entityId.contains(Constants.NOT_FOUND)) {

			// add the "homemade" error
			String details = Constants.getProvokedNotFoundErrorRational(entityId);
			addNewIssueRecord(Status.NOT_FOUND, details, NgsiStatusCodes.ContextElementNotFound);
		} // end if
	} // end checkForNotFoundError()

	/**
	 * Checks if entityId contains the String Constants.NOT_FOUND and if so,
	 * adds an issue to the request helper object stating that the context
	 * element identified by entityId could not be found.
	 */
	public void checkForExistingError(String entityId) {

		if (entityId.contains(Constants.EXISTING)) {

			// add the "homemade" error
			String details = Constants.getProvokedExistingErrorRational(entityId);
			addNewIssueRecord(Status.CONFLICT, details, NgsiStatusCodes.InvalidParameter);
		} // end if
	} // end checkForNotFoundError()
	
	
	
	
	
} // end class RequestTestHelper
