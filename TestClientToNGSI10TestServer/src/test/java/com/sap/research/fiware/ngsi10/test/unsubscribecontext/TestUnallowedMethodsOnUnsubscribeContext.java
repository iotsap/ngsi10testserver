package com.sap.research.fiware.ngsi10.test.unsubscribecontext;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnUnsubscribeContext {

	@Test
	public void getOnUnsubscribeContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.URL_UNSUBSCRIBE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void putOnUnsubscribeContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_UNSUBSCRIBE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void deleteOnUnsubscribeContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_UNSUBSCRIBE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

}
