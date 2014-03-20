package com.sap.research.fiware.ngsi10.test.responsecheckers;

import java.io.InputStream;

import noNamespace.QueryContextResponse;
import noNamespace.QueryContextResponseDocument;

public class QueryContextResponseChecker extends AbstractContextResponseChecker {

	QueryContextResponseChecker() {
	}

	@Override
	protected void readPayloadFrom(InputStream inputStream) throws Exception {
		responseDocument = QueryContextResponseDocument.Factory.parse(inputStream);
	}

	@Override
	protected void extractResponse() {
		response = ((QueryContextResponseDocument) responseDocument).getQueryContextResponse();
	}

	@Override
	protected void extractStatusCodeAndResponseList() {
		QueryContextResponse queryContextResponse = (QueryContextResponse) response;
		errorCode = queryContextResponse.getErrorCode();
		contextResponseList = queryContextResponse.getContextResponseList();
	}
}
