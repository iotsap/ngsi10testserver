package com.sap.research.fiware.ngsi10.test.contextsubscriptions;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnContextSubscriptions {

	@Test
	public void getOnContextSubscriptionsMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.URL_SUBSCRIPTIONS)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void putOnContextSubscriptionsMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_SUBSCRIPTIONS)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void deleteOnContextSubscriptionsMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_SUBSCRIPTIONS)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "POST");
	}

	@Test
	public void getOnContextSubscriptionIdMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.URL_EXAMPLE_SUBSCRIPTION_ID)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "PUT", "DELETE");
	}

	@Test
	public void postOnContextSubscriptionIdMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.POST
				.url(Constants.URL_EXAMPLE_SUBSCRIPTION_ID)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "PUT", "DELETE");
	}

}
