package com.sap.research.fiware.ngsi10.test.helpers;

import javax.ws.rs.core.Response.Status;

import noNamespace.StatusCode;

/**
 * Class for storing and keeping track of Errors (and Warnings?) found during
 * processing of requests in RequestTestHelper.
 * 
 * The class stores
 * <ol>
 * <li>a (Http) Status, which is to be returned to the ResponseBuilder (and thus
 * creates the appropriate http response),
 * <li>a NgsiStatusCode enum, from which the StatusCode is created, which then
 * is to be returned in the body of the http response, and
 * <li>a String, which is used to explain the error in the <tt>details</tt>
 * attribute of the StatusCode (the actual one, as defined by the FIWARE mapping
 * of the NGSI&nbsp;9&10 spec).
 * </ol>
 * */
public class IssueRecord {

	Status httpStatus;
	String description;
	NgsiStatusCodes ngsiStatusCode;

	final StringBuffer sb = new StringBuffer();

	IssueRecord(Status httpStatus, String desc, NgsiStatusCodes ngsiStatusCode) {
		this.httpStatus = httpStatus;
		this.description = desc;
		this.ngsiStatusCode = ngsiStatusCode;
	} // end constructor

	/**
	 * Returns the HTTP status code enum, as defined in
	 * javax.ws.rs.core.Response.Status.
	 */
	public Status getHttpStatus() {
		return httpStatus;
	}

	/**
	 * Returns the NgsiStatusCode enum, as defined in the helper classes in
	 * com.sap.research.fiware.ngsi10.test.helpers.IssueRecord.ngsiStatusCode.
	 */
	public StatusCode toStatusCode(String details) {
		return ngsiStatusCode.toStatusCode(details);
	} // end toStatusCode()

	public String toString() {
		sb.setLength(0);

		sb.append(description);
		sb.append(" (");
		sb.append("HTTP status code: ");
		sb.append(httpStatus.toString());
		sb.append(", NGSI status code: ");
		sb.append(ngsiStatusCode.toString());
		sb.append(").");

		return sb.toString();
	} // end toString()

	boolean isHttpError() {
		return httpStatus.getStatusCode() >= 400;
	}

	boolean isNgsiError() {
		return !ngsiStatusCode.equals(NgsiStatusCodes.Ok);
	}

} // end class IssueRecord
