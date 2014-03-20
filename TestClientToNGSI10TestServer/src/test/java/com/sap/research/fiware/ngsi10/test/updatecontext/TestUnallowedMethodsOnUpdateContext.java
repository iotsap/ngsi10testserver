package com.sap.research.fiware.ngsi10.test.updatecontext;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnUpdateContext {

	@Test
	public void getOnUpdateContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.URL_UPDATE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void putOnUpdateContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_UPDATE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void deleteOnUpdateContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_UPDATE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

}
