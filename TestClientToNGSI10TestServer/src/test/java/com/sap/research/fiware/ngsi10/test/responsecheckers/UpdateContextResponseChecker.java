package com.sap.research.fiware.ngsi10.test.responsecheckers;

import java.io.InputStream;

import noNamespace.UpdateContextResponse;
import noNamespace.UpdateContextResponseDocument;

public class UpdateContextResponseChecker extends AbstractContextResponseChecker {

	UpdateContextResponseChecker() {
	}

	@Override
	protected void readPayloadFrom(InputStream inputStream) throws Exception {
		responseDocument = UpdateContextResponseDocument.Factory.parse(inputStream);
	}

	@Override
	protected void extractResponse() {
		response = ((UpdateContextResponseDocument) responseDocument).getUpdateContextResponse();
	}

	@Override
	protected void extractStatusCodeAndResponseList() {
		UpdateContextResponse updateContextResponse = (UpdateContextResponse) response;
		errorCode = updateContextResponse.getErrorCode();
		contextResponseList = updateContextResponse.getContextResponseList();
	}

}
