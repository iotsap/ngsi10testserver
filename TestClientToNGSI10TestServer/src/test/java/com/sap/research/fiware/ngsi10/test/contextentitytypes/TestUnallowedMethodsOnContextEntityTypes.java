package com.sap.research.fiware.ngsi10.test.contextentitytypes;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnContextEntityTypes {

	@Test
	public void putOnTypeNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_TYPE_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void postOnTypeNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.POST
				.url(Constants.URL_EXAMPLE_TYPE_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void deleteOnTypeNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_EXAMPLE_TYPE_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}


	@Test
	public void putOnTypeNameAttributesMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void postOnTypeNameAttributesMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.POST
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void deleteOnTypeNameAttributesMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}


	@Test
	public void putOnTypeNameAttributeNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void postOnTypeNameAttributeNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.POST
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void deleteOnTypeNameAttributeNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTES_EXAMPLE_ATTRIBUTE_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}


	@Test
	public void putOnTypeNameAttributeDomainNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void postOnTypeNameAttributeDomainNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.POST
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

	@Test
	public void deleteOnTypeNameAttributeDomainNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_EXAMPLE_TYPE_NAME_ATTRIBUTE_DOMAINS_EXAMPLE_ATTRIBUTE_DOMAIN_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}

}
