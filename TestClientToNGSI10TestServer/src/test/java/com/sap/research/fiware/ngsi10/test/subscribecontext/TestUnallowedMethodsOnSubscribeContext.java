package com.sap.research.fiware.ngsi10.test.subscribecontext;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnSubscribeContext {

	@Test
	public void getOnSubscribeContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.URL_SUBSCRIBE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void putOnSubscribeContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_SUBSCRIBE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void deleteOnSubscribeContextMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_SUBSCRIBE_CONTEXT)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

}
