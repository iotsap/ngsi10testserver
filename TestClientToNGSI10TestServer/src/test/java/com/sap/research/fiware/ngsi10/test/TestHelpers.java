package com.sap.research.fiware.ngsi10.test;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

public class TestHelpers {

	public static void assertMethodNotAllowed(HttpURLConnection conn, String... expectedAllowedMethods) throws Exception {
		assertEquals(405, conn.getResponseCode());
		List<String> expectedMethods = Arrays.asList(expectedAllowedMethods);
		String allowHeaderField = conn.getHeaderField("Allow");
		assertNotNull(allowHeaderField);
		String allMethods[] = {"GET", "PUT", "POST", "DELETE"};
		for (String method : allMethods) {
			if (expectedMethods.contains(method)) {
				assertTrue(allowHeaderField.contains(method));
			} else {
				assertFalse(allowHeaderField.contains(method));
			}
		}
	}

}
