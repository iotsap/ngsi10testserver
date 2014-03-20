package com.sap.research.fiware.ngsi10.test.responsecheckers;

import java.io.InputStream;
import static org.junit.Assert.*;
import noNamespace.ContextAttributeList;
import noNamespace.ContextElement;
import noNamespace.ContextElementResponse;
import noNamespace.StatusCode;

import org.apache.xmlbeans.XmlObject;

public class GetContextEntitiesChecker<RES extends XmlObject> extends OllisAbstractResponseChecker<RES> {

	@Override
	protected void retrieveStatusCode() {
		// System.out.println("GetContextEntitiesChecker.retrieveStatusCode()");

		ContextElementResponse contextElementResponse = (ContextElementResponse) response;

		ContextElement contextElement = contextElementResponse.getContextElement();
		assertNotNull(contextElement);

		statusCode = contextElementResponse.getStatusCode();
	} // retrieveStatusCode()

	@Override
	protected void assertContent() {
		// System.out.println("GetContextEntitiesChecker.assertContent()");

		// System.out.println("Response is:" + response.toString());

		ContextElementResponse res = (ContextElementResponse) getResponse();

		// 111
		// check if, in case of error, there is superfluous data in the response

		if (statusCode.getCode() >= 400) {
			// there is an error: => must not have a <contextAttributeList>
			ContextAttributeList ctxAttList = res.getContextElement().getContextAttributeList();

			if (ctxAttList == null || ctxAttList.isNil() || ctxAttList.sizeOfContextAttributeArray() == 0) {
				// all good: there should be no list and there is none :)
			} else {
				String err = "ERROR: On an error, the ContextElement response should only carry a ststusCode and the ID field of the EntityId but carries a contextAttributeList.";
				// System.out.println(err);
				// System.out.println(ctxAttList.toString());

				throw new RuntimeException(err);
			}
		}

		// 222
		// check anything else?

	} // end assertContent()

} // end class GetContextEntitiesChecker
