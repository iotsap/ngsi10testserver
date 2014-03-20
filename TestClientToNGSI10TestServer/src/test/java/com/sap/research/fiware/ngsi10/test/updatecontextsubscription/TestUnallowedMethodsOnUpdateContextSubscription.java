package com.sap.research.fiware.ngsi10.test.updatecontextsubscription;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnUpdateContextSubscription {

	@Test
	public void getOnUpdateContextSubscriptionMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.URL_UDATE_CONTEXT_SUBSCRIPTION)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void putOnUpdateContextSubscriptionMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_UDATE_CONTEXT_SUBSCRIPTION)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void deleteOnUpdateContextSubscriptionMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_UDATE_CONTEXT_SUBSCRIPTION)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

}
