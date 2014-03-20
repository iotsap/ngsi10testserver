package com.sap.research.fiware.ngsi10.test.helpers;

import noNamespace.StatusCode;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;

public enum NgsiStatusCodes {
	Ok(200, "Ok", "This StatusCode indicates a success in the operation performer or requested."),
	//
	BadRequest(400, "Bad request", "This StatusCode indicates that the request is not well formed."),
	//
	Forbidden(403, "Forbidden", "This StatusCode indicates that the request is not allowed."),
	//
	ContextElementNotFound(404, "ContextElement not found",
			"This StatusCode indicates that the ContextElement requested is not found."),
	//
	SubscriptionIdNotFound(470, "Subscription ID not found",
			"This StatusCode indicates that the subscription ID specified does not correspond to an active subscription."),
	//
	MissingParameter(471, "Missing parameter", "This StatusCode indicates that a parameter is missing in the request."),
	//
	InvalidParameter(472, "Invalid parameter",
			"This StatusCode indicates that a parameter is not valid /allowed in the request."),
	//
	ErrorInMetadata(473, "Error in metadata",
			"This StatusCode indicates that there is a generic error in the metadata (e.g., Expires older than timestamp). "),
	//
	RegexForEntityIdNotAllowed(480, "Regular Expression for EntityId not allowed",
			"This StatusCode indicates that a regular expression for EntityId is not allowed by the receiver."),
	//
	EntityTypeRequired(481, "Entity Type required",
			"This StatusCode indicates that the EntityType is required by the receiver."),
	//
	AttributeListRequired(482, "AttributeList required",
			"This StatusCode indicates that the AttributeList is required."),

	//
	ReceiverInternalError(500, "Receiver internal error",
			"This StatusCode indicates that an unknown error at the receiver has occurred. ");

	private final int code;
	private final String reasonPhrase;
	private final String description;

	NgsiStatusCodes(int code, String reason, String description) {
		this.code = code;
		this.reasonPhrase = reason;
		this.description = description;
	}

	/**
	 * Returns a FiWare NGSI StatusCode with code and reason phrase set to the
	 * standard NGSI values. If non-null details are provided, then the provided
	 * details are used, otherwise the standard NGSI description is used. See
	 * NGSI specification, Sect 5.5.14 StatusCode Structure.
	 * */

	public StatusCode toStatusCode(String details) {
		StatusCode statusCode = StatusCode.Factory.newInstance();
		statusCode.setCode(code);
		statusCode.setReasonPhrase(reasonPhrase);

		XmlString str = XmlString.Factory.newInstance();

		if (null == details) {
			// no details given, use standard NGSI details
			str.setStringValue(description);
		} else {
			str.setStringValue(details);
		}
		XmlObject obj = statusCode.addNewDetails();
		obj.set(str);
		return statusCode;
	} // end toStatusCode()

} // end enum NgsiStatusCodes
