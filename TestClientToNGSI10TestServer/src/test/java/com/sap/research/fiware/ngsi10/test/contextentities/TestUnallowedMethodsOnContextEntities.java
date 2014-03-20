package com.sap.research.fiware.ngsi10.test.contextentities;

import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;
import com.sap.research.fiware.ngsi10.test.TestHelpers;

public class TestUnallowedMethodsOnContextEntities {

	
	/* /contextEntities/{EntityID}/attributes/{attributeName}
	 * NOT ALLOWED: PUT
	 * */
	
	@Test
	public void putOnContextEntitiyAttributeNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET", "POST", "DELETE");
	}

	/* /contextEntities/{EntityID}/attributes/{attributeName}/{valueID}
	 * NOT ALLOWED: POST
	 * */
	@Test
	public void postOnContextEntityAttributeNameValueIDMustResultInNotAllowed() throws Exception {
//		System.out.println(Constants.URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_NAME_VALUE_ID);
		HttpURLConnection conn = HttpRequestBuilder.POST
				.url(Constants.URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_NAME_VALUE_ID)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET", "PUT", "DELETE");
	}

	/* /contextEntities/{EntityID}/attributeDomains/{attributeDomainName}
	 * NOT ALLOWED: *PUT*, POST, DELETE
	 * */
	@Test
	public void putOnContextEntityAttributeDomainNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.PUT
				.url(Constants.URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_DOMAIN_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}
	
	/* /contextEntities/{EntityID}/attributeDomains/{attributeDomainName}
	 * NOT ALLOWED: PUT, *POST*, DELETE
	 * */
	@Test
	public void postOnContextEntityAttributeDomainNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.POST
				.url(Constants.URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_DOMAIN_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}
	
	/* /contextEntities/{EntityID}/attributeDomains/{attributeDomainName}
	 * NOT ALLOWED: PUT, POST, *DELETE*
	 * */
	@Test
	public void deleteOnContextEntityAttributeDomainNameMustResultInNotAllowed() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.DELETE
				.url(Constants.URL_EXAMPLE_ENTITY_ID_ATTRIBUTE_DOMAIN_NAME)
				.build();
		TestHelpers.assertMethodNotAllowed(conn, "GET");
	}
	
}
