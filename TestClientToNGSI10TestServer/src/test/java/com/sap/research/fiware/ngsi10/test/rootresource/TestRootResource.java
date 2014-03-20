package com.sap.research.fiware.ngsi10.test.rootresource;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.junit.Test;

import com.sap.research.fiware.ngsi10.test.Constants;
import com.sap.research.fiware.ngsi10.test.HttpRequestBuilder;

public class TestRootResource {

	@Test
	public void getOnRootResource() throws Exception {
		HttpURLConnection conn = HttpRequestBuilder.GET
				.url(Constants.NGSI10_BASE_URL)
				.build();
		assertEquals(200, conn.getResponseCode());
		assertEquals("text/plain", conn.getContentType());
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine = in.readLine();
		assertNotNull(inputLine);
		assertEquals("Welcome to the NGSI10 Test Server!", inputLine);
		inputLine = in.readLine();
		assertNull(inputLine);
		in.close();
	}

}
