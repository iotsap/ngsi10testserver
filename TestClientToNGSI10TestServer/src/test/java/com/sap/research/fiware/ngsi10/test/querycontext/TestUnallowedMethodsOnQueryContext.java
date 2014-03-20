package com.sap.research.fiware.ngsi10.test.querycontext;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnQueryContext {

	@Test
	public void getOnQueryContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.URL_QUERY_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void putOnQueryContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_QUERY_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void deleteOnQueryContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_QUERY_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

}
