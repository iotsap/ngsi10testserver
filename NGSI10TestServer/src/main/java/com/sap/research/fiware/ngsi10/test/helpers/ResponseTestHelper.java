package com.sap.research.fiware.ngsi10.test.helpers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import noNamespace.ContextElement;
import noNamespace.ContextElementResponse;
import noNamespace.ContextElementResponseDocument;
import noNamespace.EntityId;
import noNamespace.NotifyContextResponse;
import noNamespace.NotifyContextResponseDocument;
import noNamespace.QueryContextResponse;
import noNamespace.QueryContextResponseDocument;
import noNamespace.StatusCode;
import noNamespace.StatusCodeDocument;
import noNamespace.SubscribeError;
import noNamespace.UpdateContextSubscriptionResponse;
import noNamespace.UpdateContextSubscriptionResponseDocument;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

public class ResponseTestHelper<RES extends XmlObject> {

	private Logger logger = Logger.getLogger(this.getClass());
	private StringBuilder buf = new StringBuilder();

	// xmlbeans XML options
	// Create an XmlOptions instance and set the error listener.
	ArrayList<XmlError> xmlParseErrorList = new ArrayList<XmlError>();
	XmlOptions xmlValidateOptions = new XmlOptions().setErrorListener(xmlParseErrorList);

	ArrayList<String> on1stLevelStatus = new ArrayList<String>(Arrays.asList("UnsubscribeContextResponse",
			"ContextElementResponse", "ContextAttributeResponse"));
	ArrayList<String> on1stLevelResponse = new ArrayList<String>(Arrays.asList("NotifyContextResponse"));
	ArrayList<String> on1stLevelError = new ArrayList<String>(Arrays.asList("QueryContextResponse",
			"UpdateContextResponse", "UpdateContextElementResponse", "UnsubscribeContextResponse"));

	// Doc has a SubscribeError, which has an error code
	// SubscribeContextResponse, UpdateContextSubscriptionResponse
	ArrayList<String> on2ndLevelError = new ArrayList<String>(Arrays.asList("SubscribeContextResponse",
			"UpdateContextSubscriptionResponse"));

	/**
	 * Validates a subclass of XmlObject according to the schema definition from
	 * which if (i.e., the XmlObject subclass) was created. Returns true if the
	 * object is valid XML according to the schema, false otherwise. In case of
	 * invalid XML, the error descriptions are put into the <tt>errors</tt> List
	 * as Strings.
	 * 
	 * @param xmlObj
	 *            The XmlObject to be validated
	 * @param errors
	 *            List of Strings describing the error.
	 * 
	 */
	public boolean isValidResponse(RES xmlObj, List<String> errors) {

		// If the XML isn't valid, loop through the listener's contents,
		// printing contained messages.

		StringBuffer errStr = new StringBuffer();
		// Validate the XML.
		xmlParseErrorList.clear();
		boolean isValid = xmlObj.validate(xmlValidateOptions);
		if (!isValid) {
			errStr.append("INTERNAL SERVER ERROR: The response " + xmlObj.schemaType()
					+ " is INVALID according to the xsd schema definition.");
			for (XmlError error : xmlParseErrorList) {
				errStr.append("\nError message: " + error.getMessage() + ". ");
				errStr.append("Location of invalid XML: " + error.getCursorLocation().xmlText() + ".");
			}
			errStr.append("\n");
			errors.add(errStr.toString());
		}
		return isValid;
	} // end isValidResponse()

	/***/
	private RES buildResponseExample(StatusCode statusCode) {

		if (true)
			throw new RuntimeException("method not implemented!");

		// EXAMPLES OF HOW ITS DONE WITHOUT REFLECTION

		// CASE 0: Response IS a StatusCode
		StatusCodeDocument resDoc0 = StatusCodeDocument.Factory.newInstance();
		resDoc0.setStatusCode(statusCode);

		// CASE 1: Response HAS a *statusCode* of type StatusCode
		ContextElementResponseDocument resDoc1 = ContextElementResponseDocument.Factory.newInstance();
		ContextElementResponse res1 = resDoc1.addNewContextElementResponse();
		res1.setStatusCode(statusCode); // <====

		// CASE 2: Response HAS a *errorCode* of type StatusCode
		QueryContextResponseDocument resDoc2 = QueryContextResponseDocument.Factory.newInstance();
		QueryContextResponse res2 = resDoc2.addNewQueryContextResponse();
		res2.setErrorCode(statusCode); // <====

		// CASE 3: Response HAS a subscribeError (class SubscribeError), which
		// has a *statusCode* of type StatusCode
		// only for UpdateContextSubscriptionResponseDocument and
		// SubscribeContextResponseDocument
		UpdateContextSubscriptionResponseDocument resDoc3 = UpdateContextSubscriptionResponseDocument.Factory
				.newInstance();
		UpdateContextSubscriptionResponse res3 = resDoc3.addNewUpdateContextSubscriptionResponse();
		SubscribeError subscribeError = res3.addNewSubscribeError(); // <====
		subscribeError.setErrorCode(statusCode); // <====

		// SubscribeContextResponseDocument resDoc3
		// =SubscribeContextResponseDocument.Factory.newInstance();
		// SubscribeContextResponse res3 =
		// resDoc3.addNewSubscribeContextResponse();
		// SubscribeError subscribeError = res3.addNewSubscribeError();
		// subscribeError.setErrorCode(statusCode);

		// CASE 4: Response HAS a *responseCode* of type StatusCode
		// only for NotifyContextResponse
		NotifyContextResponseDocument resDoc4 = NotifyContextResponseDocument.Factory.newInstance();
		NotifyContextResponse res4 = resDoc4.addNewNotifyContextResponse();
		res4.setResponseCode(statusCode); // <====

		return null;
	} // end buildResponse()

	/**
	 * 
	 * Generates and returns a Response based on <tt>responseDoc</tt> and error
	 * information stored in the RequestTestHelper (for both the HTTP status and
	 * NGSI Status).
	 * 
	 * If there have been no issues (i.e., errors) set to the RequestTestHelper,
	 * then both status codes will be OK (200). The caller has to make sure that
	 * the content of the responseDoc and the status codes make sense together.
	 * To check which issue will be used in generating the status codes, use
	 * getFisrtError() on reqHlp.
	 * 
	 * To set an issue, use addNewIssueRecord() on reqHlp. Yes, that is the
	 * RequestTestHelper!
	 * 
	 * */
	public Response toRestResponse(RequestTestHelper<? extends XmlObject> reqHlp, RES responseDoc) {

		String detail = reqHlp.getRequestInformation();
		Status httpStatusCode;
		StatusCode ngsiStatusCode;

		if (reqHlp.hasErrors()) {
			// get the first error in this request and set both status codes
			// accordingly
			IssueRecord issue = reqHlp.getFirstError();
			httpStatusCode = issue.getHttpStatus();
			ngsiStatusCode = issue.toStatusCode(detail);

		} else {
			// both OK if no errors
			ngsiStatusCode = NgsiStatusCodes.Ok.toStatusCode(detail);
			httpStatusCode = Status.OK;
		} // end else

		responseDoc = addStatusCode(responseDoc, ngsiStatusCode);
		return Response.status(httpStatusCode).entity(responseDoc.toString()).build();

	} // end buildErrorResponse()

	/**
	 * Takes an instance of the IMPLEMENTATION of the class RES (which has to be
	 * one of the FIWARE NSGI response classes the name of which ends in
	 * "DocumentImpl") and sets the given StatusCode to it, creating the
	 * required fields along the way if required. Note that any pre-existing
	 * statusCode will be mercilessly overwritten. <strong>Note</strong> that
	 * this method DOES NOT necessarily create responses that are valid
	 * according to the XML schema or the FIWARE NGSI binding.
	 * <p>
	 * 
	 * For example, a ContextElementResponseDocument contains a
	 * ContextElementResponse, which in turn contains a StatusCode. This method
	 * will create the ContextElementResponse (using
	 * ContextElementResponseDocument's method addNewContextElementResponse) and
	 * then set the StatusCode to the returned object (using the method
	 * setStatusCode() ).
	 * <p>
	 * 
	 * Sometimes the StatusCode is stored in a field errorCode or responseCode.
	 * In this case the setter is called setErrorCode() and setResponseCode(),
	 * respectively. This method takes care of this.
	 * 
	 * The most complex case is this (which is also handled):<code><pre>
UpdateContextSubscriptionResponseDocument resDoc3 = UpdateContextSubscriptionResponseDocument.Factory.newInstance();
UpdateContextSubscriptionResponse res3 = resDoc3.addNewUpdateContextSubscriptionResponse();
SubscribeError subscribeError = res3.addNewSubscribeError(); // <====
subscribeError.setErrorCode(statusCode); // <====
	 * </pre></code>
	 * 
	 * @param res
	 *            the response (a FIWARE NGSI response "XXXResponseDocumentImpl"
	 *            or "StatusCodeDocument") to add the StatusCode to.
	 * @param statusCode
	 *            the StatusCode to set to res
	 * */
	private RES addStatusCode(RES resDoc, StatusCode statusCode) {

		String resDocClassName = resDoc.getClass().getSimpleName();
		// we are either dealing with a "SatusCode(DocumentImpl)" or a
		// "XXXResponse(DocumentImpl)"

		//
		// Handling StatusCodeDocumnet responses
		//
		if (resDocClassName.equals("StatusCodeDocumentImpl")) {
			setStatusCodeByMethodname(resDoc, "setStatusCode", statusCode);
			// throw new
			// RuntimeException("Creating StatucCode as Response not yet implemented");
			return resDoc;
		}

		// sanity check
		if (!resDocClassName.endsWith("ResponseDocumentImpl")) {

			buf.setLength(0);
			buf.append(this.getClass().getSimpleName());
			buf.append(" can only handle XmlObject types ending in \"DocumentImpl\" (such as StatusCodeDocumentImpl), found ");
			buf.append(resDocClassName);

			logger.error(buf.toString());
			// XXX: do an internal server error more gracefully then just
			// throwing an exception... :-)
			throw new RuntimeException(buf.toString());
		}

		//
		// Handling all other responses
		//

		//
		// create subclass without "Document" in name: this is the same for all
		// document classes excluding StatusCode. for StatusCode we can attach
		// the statusCode directly.
		//

		// name of the class without the "DocumentImpl"-- this class we need to
		// create...
		String targetClassName = resDocClassName.substring(0, resDocClassName.length() - "DocumentImpl".length());

		// create the target class in resDoc
		XmlObject lvl1Object = getOrCreateNamedClassIn(resDoc, targetClassName);

		// Now distinguish between where the status code is (at 1st or 2nd
		// level--the root case has been handled above in StatusCode already)
		// and how its called (statusCode, errorCode, or responseCode).
		if (on1stLevelStatus.contains(targetClassName)) {
			// lvl1Object has a setStatusCode() method
			setStatusCodeByMethodname(lvl1Object, "setStatusCode", statusCode);

		} else if (on1stLevelError.contains(targetClassName)) {
			// retObj has a setErrorCode() method
			setStatusCodeByMethodname(lvl1Object, "setErrorCode", statusCode);

		} else if (on1stLevelResponse.contains(targetClassName)) {
			// lvl1Object has a setResponseCode() method
			setStatusCodeByMethodname(lvl1Object, "setResponseCode", statusCode);

		} else if (on2ndLevelError.contains(targetClassName)) {
			// has subscribeError of type SubscribeError, which in turn has an
			// errorCode of type StatusCode

			XmlObject lvl2Object = getOrCreateNamedClassIn(lvl1Object, "SubscribeError");

			// this should be a SubscribeError to which we will set the
			// StatusCode, which happens to be named errorCode
			setStatusCodeByMethodname(lvl2Object, "setErrorCode", statusCode);

		} else {

			// PANIC
			throw new RuntimeException("Unknown class " + targetClassName
					+ ". Dunno where to attach status code to it :(");
		}

		return resDoc;
	} // end buildResponse()

	/**
	 * Returns a class with name <tt>targetClassName</tt> by invoking a method
	 * named "addNew" + targetClassName (or "get" + targetClassName) on the
	 * object <tt>in</tt> via Java reflection. That is, if the "get" method
	 * returns null, a new object is created and returned, otherwise the result
	 * of the get method is returned.
	 * */

	XmlObject getOrCreateNamedClassIn(XmlObject in, String targetClassName) {

		String methodNameAddNew = "addNew" + targetClassName;
		String methodNameGet = "get" + targetClassName;

		XmlObject lvl1Object = null;

		// is the field set already?
		try {
			Method getMethod = in.getClass().getMethod(methodNameGet, (Class<?>[]) null);
			lvl1Object = (XmlObject) getMethod.invoke(in, (Object[]) null);

			if (lvl1Object == null) {
				// not already set, so we need to create it
				Method addNewMethod = in.getClass().getMethod(methodNameAddNew, (Class<?>[]) null);
				lvl1Object = (XmlObject) addNewMethod.invoke(in, (Object[]) null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		// not already set, need to create it
		// try {
		// Method addNewMethod = in.getClass().getMethod(methodNameAddNew,
		// (Class<?>[]) null);
		// lvl1Object = (XmlObject) addNewMethod.invoke(in, (Object[]) null);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw new RuntimeException(e);
		// }

		// don't do System.out.println on the server side!
		/*
		if (lvl1Object == null) {
			// we failed at last :(
			System.out.println("ERROR: something went wrong... couldn't create " + targetClassName + " :(");
			return null;
		} else {
//			System.out.println("Created object of class " + lvl1Object.getClass().getSimpleName() + ": "
//					+ lvl1Object.toString());
		}
		*/

		return lvl1Object;
	} // end createNamedClassIn()

	/**
	 * Sets the StatusCode <tt>statusCode</tt> to the <tt>target</tt> by
	 * invoking the method known by the name <tt>methodName</tt> (one of
	 * "setStatusCode", "setErrorCode", or "setResponseCode") on the
	 * <tt>target</tt> and passing the <tt>statusCode<tt>.
	 * 
	 * */
	ArrayList<String> setMethods = new ArrayList<String>(Arrays.asList("setStatusCode", "setErrorCode",
			"setResponseCode"));

	void setStatusCodeByMethodname(XmlObject target, String methodName, StatusCode statusCode) {

		if (!setMethods.contains(methodName)) {
			throw new RuntimeException("Can only handle one of the following method names: " + setMethods.toString());
		}

		try {
			Method setMethod = target.getClass().getMethod(methodName, noNamespace.StatusCode.class);
			setMethod.invoke(target, statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	} // end setStatusCodeByName()

	/* just a test */

	public static void main(String[] args) {
		StatusCode statusCode = NgsiStatusCodes.Ok.toStatusCode("hi there, this is a test.");

		// TESTING THE buildResponse() method, which uses reflection

		//
		// CASE 0: Response IS a StatusCode
		//
		ResponseTestHelper<StatusCodeDocument> hlp0 = new ResponseTestHelper<StatusCodeDocument>();
		StatusCodeDocument resDoc0 = StatusCodeDocument.Factory.newInstance();
		resDoc0 = hlp0.addStatusCode(resDoc0, statusCode);
		System.out.println("xml of response doc is:\n" + resDoc0.xmlText(Constants.ppOpts));

		//
		// CASE 1a: Response HAS a *statusCode* of type StatusCode
		//
		ResponseTestHelper<ContextElementResponseDocument> hlp1A = new ResponseTestHelper<ContextElementResponseDocument>();
		ContextElementResponseDocument resDoc1A = ContextElementResponseDocument.Factory.newInstance();
		resDoc1A = hlp1A.addStatusCode(resDoc1A, statusCode);
		System.out.println("xml of response doc is:\n" + resDoc1A.xmlText(Constants.ppOpts));

		// test the same but add some stuff to the doc beforehand... then check
		// if it's still there
		hlp1A = new ResponseTestHelper<ContextElementResponseDocument>();
		resDoc1A = ContextElementResponseDocument.Factory.newInstance();

		ContextElementResponse res = resDoc1A.addNewContextElementResponse();
		ContextElement el = res.addNewContextElement();
		el.setAttributeDomainName("attributeDomainName");
		EntityId id = el.addNewEntityId();
		id.setId("id");
		id.setType("type");
		resDoc1A = hlp1A.addStatusCode(resDoc1A, statusCode);
		System.out.println("xml of response doc is:\n" + resDoc1A.xmlText(Constants.ppOpts));

		//
		// CASE 1b: Response HAS a *errorCode* of type StatusCode
		//
		ResponseTestHelper<QueryContextResponseDocument> hlp1B = new ResponseTestHelper<QueryContextResponseDocument>();
		QueryContextResponseDocument resDoc1B = QueryContextResponseDocument.Factory.newInstance();
		resDoc1B = hlp1B.addStatusCode(resDoc1B, statusCode);
		System.out.println("xml of response doc is:\n" + resDoc1B.xmlText(Constants.ppOpts));

		//
		// CASE 1c: Response HAS a *responseCode* of type StatusCode
		// only for NotifyContextResponse
		//
		ResponseTestHelper<NotifyContextResponseDocument> hlp1C = new ResponseTestHelper<NotifyContextResponseDocument>();
		NotifyContextResponseDocument resDoc1C = NotifyContextResponseDocument.Factory.newInstance();
		resDoc1C = hlp1C.addStatusCode(resDoc1C, statusCode);
		System.out.println("xml of response doc is:\n" + resDoc1C.xmlText(Constants.ppOpts));

		//
		// CASE 2: Response HAS a subscribeError (class SubscribeError), which
		// has a *statusCode* of type StatusCode only for
		// UpdateContextSubscriptionResponseDocument and
		// SubscribeContextResponseDocument
		//

		ResponseTestHelper<UpdateContextSubscriptionResponseDocument> hlp2 = new ResponseTestHelper<UpdateContextSubscriptionResponseDocument>();
		UpdateContextSubscriptionResponseDocument resDoc2 = UpdateContextSubscriptionResponseDocument.Factory
				.newInstance();
		resDoc2 = hlp2.addStatusCode(resDoc2, statusCode);
		System.out.println("xml of response doc is:\n" + resDoc2.xmlText(Constants.ppOpts));

		// UpdateContextSubscriptionResponseDocument resDoc3 =
		// UpdateContextSubscriptionResponseDocument.Factory
		// .newInstance();
		// UpdateContextSubscriptionResponse res3 =
		// resDoc3.addNewUpdateContextSubscriptionResponse();
		// SubscribeError subscribeError = res3.addNewSubscribeError(); // <====
		// subscribeError.setErrorCode(statusCode); // <====

	} // end main

} // end class ResponseTestHelper
