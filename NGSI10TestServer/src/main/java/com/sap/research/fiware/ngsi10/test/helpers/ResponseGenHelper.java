package com.sap.research.fiware.ngsi10.test.helpers;

import javax.ws.rs.core.Response.Status;

import noNamespace.ContextAttribute;
import noNamespace.ContextAttributeList;
import noNamespace.ContextAttributeResponse;
import noNamespace.ContextAttributeResponseList;
import noNamespace.ContextElement;
import noNamespace.ContextElementResponse;
import noNamespace.ContextElementResponseDocument;
import noNamespace.ContextMetadata;
import noNamespace.ContextMetadataList;
import noNamespace.StatusCode;
import noNamespace.StatusCodeDocument;
import noNamespace.UpdateContextElementRequest;
import noNamespace.UpdateContextElementRequestDocument;
import noNamespace.UpdateContextElementResponse;
import noNamespace.UpdateContextElementResponseDocument;

import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlString;

public class ResponseGenHelper {

	/*
	 * STATUS CODE
	 */

	/**
	 * Generates a StatusCodeDocument from the parameters; to be used as a
	 * response directly.
	 * */
	public static StatusCodeDocument genStatusCodeDoc(StatusCode sC) {
		StatusCodeDocument statusCodeDocument = StatusCodeDocument.Factory.newInstance();
		statusCodeDocument.setStatusCode(sC);
		return statusCodeDocument;
	} // end genStatusCodeDoc()

	/*
	 * CONTEXT ATTRIBUTE
	 */

	/*
	 * CONTEXT ELEMENT RESPONSE
	 */

	/**
	 * Generate a ContextElementResponseDocument with a ContextElement and
	 * StatusCode. The ContextElement only contains the entityId, the StatusCode
	 * contains the status code of the given <tt>status</tt> and <tt>phrase</tt>
	 * .
	 * 
	 * */
	public static ContextElementResponseDocument generateStandardContextElementResponseDocument(String entityId) {

		ContextElementResponseDocument doc = ContextElementResponseDocument.Factory.newInstance();
		ContextElementResponse ctxElRes = doc.addNewContextElementResponse();

		// 2 mandatory fields: ContextElement and StatusCode

		ContextElement ctxEl = ctxElRes.addNewContextElement();

		// add entity id
		ctxEl.addNewEntityId().setId(entityId);
		ctxEl.setAttributeDomainName("in-house");

		// add attributes
		ContextAttributeList contextAttributeList = ctxEl.addNewContextAttributeList();

		// XXX use convenience method generateSampleContextAttribute()!
		/*
		 * ContextAttribute temperature =
		 * contextAttributeList.addNewContextAttribute();
		 * temperature.setName("temperature");
		 * temperature.addNewContextValue().set
		 * (XmlInteger.Factory.newValue(21));
		 * 
		 * ContextAttribute humidity =
		 * contextAttributeList.addNewContextAttribute();
		 * humidity.setName("power consumption");
		 * humidity.addNewContextValue().set(XmlInteger.Factory.newValue(5000));
		 */
		ContextAttribute[] contextAttributeArray = new ContextAttribute[3];
		for (int i = 0; i < 3; i++) {
			contextAttributeArray[i] = generateSampleContextAttribute(i);
		}
		contextAttributeList.setContextAttributeArray(contextAttributeArray);

		// no need to set StatusCode, that's done in toRestResponse()

		return doc;
	} // end generateStandardContextElementResponseDocument()

	/**
	 * Generates a ContextAttribute object and fills it with one of several
	 * possible "sample" (i.e., mock, mad-up, fake) value sets. The parameter i
	 * determines which value set is used. i=0 is temperature, i=1 is humidity,
	 * i=2 is power consumption.
	 * 
	 * The returned object meets the requirement of having an
	 * "ID in the form of metadata"
	 * 
	 * @see #fillSampleContextAttribute
	 */
	public static ContextAttribute generateSampleContextAttribute(int i) {

		ContextAttribute ret = ContextAttribute.Factory.newInstance();

		return fillSampleContextAttribute(ret, i);

	} // end generateSampleContextAttribute()

	/**
	 * Fills the given ContextAttribute object with one of several possible
	 * "sample" (i.e., mock, mad-up, fake) value sets. The parameter i
	 * determines which value set is used. i=0 is temperature, i=1 is humidity,
	 * i=2 is power consumption.
	 * 
	 * The returned object meets the requirement of having an
	 * "ID in the form of metadata"
	 * 
	 * @see #generateSampleContextAttribute
	 */

	public static ContextAttribute fillSampleContextAttribute(ContextAttribute ret, int i) {
		i = i % 2;

		switch (i) {
		case 0:
			ret.setName("temperature");
			ret.addNewContextValue().set(XmlInteger.Factory.newValue(21));
			break;

		case 1:
			ret.setName("humidity");
			ret.addNewContextValue().set(XmlInteger.Factory.newValue(75));
			break;

		case 2:
			ret.setName("power consumption");
			ret.addNewContextValue().set(XmlInteger.Factory.newValue(5000));
			break;

		default:
			ret.setName("???");
			ret.addNewContextValue().set(XmlInteger.Factory.newValue(666));
			break;

		} // end switch

		// add "id as metadata"
		addIdAsMetadata(ret, null);
		return ret;
	}

	/**
	 * Creates an UpdateContextElementRequestDocument will all fields filled
	 * (with arbitrary values).
	 * */
	public static UpdateContextElementRequestDocument generateStandardUpdateContextElementRequestDocument() {

		UpdateContextElementRequestDocument doc = UpdateContextElementRequestDocument.Factory.newInstance();
		UpdateContextElementRequest req = doc.addNewUpdateContextElementRequest();

		// 1: DOMAIN NAME
		req.setAttributeDomainName("attributeDomainName");

		// 2: CONTEXT ATTRIBUE LIST
		ContextAttributeList ctxAList = req.addNewContextAttributeList();
		// add 3 attribute objects
		for (int i = 0; i < 3; i++) {
			ContextAttribute ctxA = ctxAList.addNewContextAttribute();
			ctxA.setName("context attribute name " + i);
			ctxA.setType("context attribute txpe " + i);
			XmlString xmlStr = XmlString.Factory.newValue("context attribute value " + i);
			ctxA.setContextValue(xmlStr);
			ContextMetadataList ctxMList = ctxA.addNewMetadata();
			// add 2 metadata objects
			for (int j = 0; j < 2; j++) {
				ContextMetadata ctxMeta = ctxMList.addNewContextMetadata();
				ctxMeta.setName("context metadata name " + j + " (for context attribute " + i + ")");
				ctxMeta.setType("context metadata type " + j + " (for context attribute " + i + ")");
				XmlString xmlStr2 = XmlString.Factory.newValue("context metadata value " + j
						+ " (for context attribute " + i + ")");
				ctxMeta.setValue(xmlStr2);
			}
			// add ID as metadata
			ResponseGenHelper.addIdAsMetadata(ctxA, null);
		}

		// 3 DOMAIN METADATA (is a list, actually)
		ContextMetadataList ctxMList = req.addNewDomainMetadata();
		// add 10 metadata objects
		for (int i = 0; i < 3; i++) {
			ContextMetadata ctxMeta = ctxMList.addNewContextMetadata();
			ctxMeta.setName("domain metadata name " + i);
			ctxMeta.setType("domain metadata type " + i);
			XmlString xmlStr = XmlString.Factory.newValue("domain metadata value " + i);
			ctxMeta.setValue(xmlStr);
		}

		return doc;
	} // end generateStandardUpdateContextElementRequestDocument()

	/**
	 * Builds an UpdateContextElementResponseDocument based on the associated
	 * request. Assumes that all update operations succeed, unless the
	 * "id in form of metadata" contains the string defined in
	 * Constants.NOT_FOUND.
	 * 
	 * */
//	public static UpdateContextElementResponseDocument generateUpdateContextElementResponseDocument(
//			UpdateContextElementRequestDocument reqDoc, RequestTestHelper<UpdateContextElementRequestDocument> reqHlp) {
		
	public static UpdateContextElementResponseDocument generateUpdateContextElementResponseDocument(
			RequestTestHelper<UpdateContextElementRequestDocument> reqHlp) {
		
		//
		// create Response
		//
		UpdateContextElementResponseDocument resDoc = UpdateContextElementResponseDocument.Factory.newInstance();
		UpdateContextElementResponse res = resDoc.addNewUpdateContextElementResponse();

		// UpdateContextElementResponse EITHER has errorCode of type StatusCode or contextResponseList of type ContextAttributeResponseList

		// create the list of contextResponseList while iterating through
		// the ContextAttributes in the request, checking for errors. if there
		// are no errors, discard the list and set the errorCode to OK. (sounds
		// stupid, IS stupid, but is indeed specified like that).


		// Request stuff...
		UpdateContextElementRequestDocument reqDoc = reqHlp.getReqDoc();
		UpdateContextElementRequest req = reqDoc.getUpdateContextElementRequest();
		ContextAttributeList ctxAList = req.getContextAttributeList();

		// is there a ContextAttributeList anyway and does it contain entries?
		if (null == ctxAList || ctxAList.isNil() || ctxAList.sizeOfContextAttributeArray() == 0) {
			// XXX: ERROR, need to set http status code accordingly
			// IssueRecord rec = new IssueRecord(Status.OK, "laber laber",
			// NgsiStatusCodes.AttributeListRequired);
			String err = Constants.ERROR_UPDATE_CONTEXT_ELEMENT_ATTRIBUTE_LIST_MISSING;
			reqHlp.addNewIssueRecord(Status.BAD_REQUEST, err, NgsiStatusCodes.AttributeListRequired);
			return resDoc;
		}

		
		// the response = target list
		ContextAttributeResponseList ctxAttResList = ContextAttributeResponseList.Factory.newInstance(); // target list
		ContextAttribute ctxAA[] = ctxAList.getContextAttributeArray(); // source list
		boolean hasErrors = false;
		for (ContextAttribute ctxA : ctxAA) {
			// for every ContextAttribute in the request, add one
			// ContextAttributeResponse in the response
			ContextAttributeResponse ctrAR = ctxAttResList.addNewContextAttributeResponse();

			// get array of metadata
			ContextMetadata[] ctxMA = ctxA.getMetadata().getContextMetadataArray();
			String id = null;
			for (ContextMetadata ctxM : ctxMA) {
				if (ctxM.getName().equals(Constants.ID)) {
					id = ctxM.getValue().toString();
					break;
				}
			} // end for

			if (null == id) {
				// update operation failed because no Metadata with
				// name== "ID" was provided

				ctrAR.setStatusCode(NgsiStatusCodes.ContextElementNotFound
						.toStatusCode(Constants.ID_IN_FORM_OF_METADATA_MISSING));
				hasErrors = true;
			} else if (id.contains(Constants.NOT_FOUND)) {
				// found the id but it contained the String Constants.NOT_FOUND,
				// so we pretend we couldn't find it
				ctrAR.setStatusCode(NgsiStatusCodes.ContextElementNotFound
						.toStatusCode(Constants.ID_IN_FORM_OF_METADATA_NOT_FOUND));
				hasErrors = true;
			} else {
				// update operation success -> add ContextAttribute

				// code for new version of XSDs
				ContextAttributeList contextAttributeList = ctrAR.addNewContextAttributeList();
				contextAttributeList.setContextAttributeArray(new ContextAttribute[] { ctxA });
			}
		} // end for

		if (hasErrors) {
			res.setContextResponseList(ctxAttResList);
		} else {
			res.setErrorCode(NgsiStatusCodes.Ok.toStatusCode("details go here!"));
		}
		return resDoc;
	}

	/**
	 * Adds, to a ContextAttribute, a ContextMetadata object, that fulfills the
	 * requirement of being a "ID in the form of metadata".
	 * 
	 * Every ContextAttribute MUST have an associated ContextMetadata, which has
	 * the name \"ID\" and a value that uniquely identifies the
	 * ContextAttribute. (See Sect. 1.2.2 Attribute Value Instances for
	 * details.) This method add this ID as metadata to the ContextAttribute,
	 * preserving any previously added metadata objects of the ContextAttribute.
	 * 
	 * @param ctxA
	 *            the ContextAttribute to attach the "ID as metadata" to
	 * @param id
	 *            the value of the id, or null (then a value will be generated)
	 * 
	 */

	public static void addIdAsMetadata(ContextAttribute ctxA, String id) {
		ContextMetadataList ctxMList = ctxA.getMetadata();
		if (null == ctxMList) {
			ctxMList = ctxA.addNewMetadata();
		}
		if (null == id || id.isEmpty()) {
			id = String.valueOf(nextIdAsMetadata());
		}

		XmlString idValue = XmlString.Factory.newValue(id);

		ContextMetadata ctxMeta = ctxMList.addNewContextMetadata();
		ctxMeta.setName(Constants.ID);
		ctxMeta.setType(Constants.XSD_STRING);
		ctxMeta.setValue(idValue);
	} // end addIdAsMetadata()

	private static int _id = new java.util.Random().nextInt();

	public static int nextIdAsMetadata() {
		return ++_id;
	}

} // end class ResponseGenHelper
