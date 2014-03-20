package com.sap.research.fiware.ngsi10.test.helpers;

import org.apache.xmlbeans.XmlOptions;

/**
 * Public constants.
 */
public class Constants {

	/**
	 * "a value ID in the form of metadata".... meaning: A ContextAttribute
	 * SHALL have ContextMetadata associated to it, which has a name "ID". The
	 * value is the actual id. The type should be xsd:String. The id uniquely
	 * identifies the ContextAttribute since there can be different attributes
	 * with the same name.
	 */
	public static final String ID = "ID";
	public static final String XSD_STRING = "xsd:string";

	// XmlOptions instance for Pretty Printing
	public static final XmlOptions ppOpts = new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(4)
			.setSavePrettyPrintOffset(8);

	/**
	 * If this string is contained in a request body, the request will be
	 * treated as an error and the response will be accordingly. This is a way
	 * to provoke "resource not found" errors from the server in order to test a
	 * client to handle this kind of error correctly.
	 * 
	 * If this string is not contained in the request body, the request will be
	 * considered to be successful and mockup values are returned in the
	 * response.
	 * */
	public static final String NOT_FOUND = "NOT_FOUND";

	public static final String getProvokedNotFoundErrorRational(String entityId) {

		return "Resource for ContextElement \"" + entityId
				+ "\" not found. *You* have provoked this error by specifying a ContextElement (i.e., an entityId)"
				+ " containing the string \"" + Constants.NOT_FOUND + "\" in the request URL (here: \"" + entityId
				+ "\")";
	}

	/**
	 * If this string is contained in a request body, the request will be
	 * treated as an error and the response will be accordingly. This is a way
	 * to provoke "conflict" errors (http status code 409) from the server in
	 * order to test a client to handle this kind of error correctly.
	 * 
	 * If this string is not contained in the request body, the request will be
	 * considered to be successful and mockup values are returned in the
	 * response.
	 * */
	public static final String EXISTING = "EXISTING";

	public static final String getProvokedExistingErrorRational(String entityId) {

		return "Resource for ContextElement \"" + entityId
				+ "\" already exists. *You* have provoked this error by specifying a ContextElement (i.e., an entityId)"
				+ " containing the string \"" + Constants.EXISTING+ "\" in the request URL (here: \"" + entityId
				+ "\")";
	}

	/**
	 * Error description for when the "id in form of metadata" is missing from a
	 * ContextAttribute
	 */
	public static final String ID_IN_FORM_OF_METADATA_MISSING = "ContextAttribute \"Id in form of metadata\" missing. "
			+ "NGSI-10 allows the existence of multiple attributes having the same name. "
			+ "The way to distinguish these attribute values - called value instances - "
			+ "is to include an ID in the form of metadata. "
			+ "That is, ever ContextAttribute MUST have an associated ContextMetadata, which "
			+ "has the name \"ID\" and a value that uniquely identifies the ContextAttribute. "
			+ "This Id is missing in the request. "
			+ " For more info see Sect. \"1.2.2 Attribute Value Instances for details\".";

	/**
	 * Error description for when the "id in form of metadata" is not found in a
	 * ContextAttribute
	 */
	public static final String ID_IN_FORM_OF_METADATA_NOT_FOUND = "Attribute Id \"Id in form of metadata\" not found. "
			+ "NGSI-10 allows the existence of multiple attributes having the same name. "
			+ "The way to distinguish these attribute values - called value instances - "
			+ "is to include an ID in the form of metadata. "
			+ "That is, ever ContextAttribute MUST have an associated ContextMetadata, which "
			+ "has the name \"ID\" and a value that uniquely identifies the ContextAttribute. "
			+ "To update an existing ContextAttriute, its Id must be specified. The Id you specified could not be found."
			+ " For more info see Sect. \"1.2.2 Attribute Value Instances for details\".";

	public static final String ERROR_UPDATE_CONTEXT_ELEMENT_ATTRIBUTE_LIST_MISSING = "AttributeList required. "
			+ " To update ContextElements you need to provide an AttributeList in which the changes are specified. "
			+ " This list is missing.";

} // end class Constants
