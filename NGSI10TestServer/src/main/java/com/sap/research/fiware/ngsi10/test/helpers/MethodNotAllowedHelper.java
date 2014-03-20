package com.sap.research.fiware.ngsi10.test.helpers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MethodNotAllowedHelper {

	public static Response onlyGetAllowed() {
		return createMethodNotAllowedResponse("GET");
	}

	public static Response onlyPostAllowed() {
		return createMethodNotAllowedResponse("POST");
	}

	public static Response onlyPutDeleteAllowed() {
		return createMethodNotAllowedResponse("PUT", "DELETE");
	}

	public static Response onlyGetPostDeleteAllowed() {
		return createMethodNotAllowedResponse("GET", "POST", "DELETE");
	}
	public static Response onlyGetPutDeleteAllowed() {
		return createMethodNotAllowedResponse("GET", "PUT", "DELETE");
	}

	private static Response createMethodNotAllowedResponse(String... allowedMethods) {
		if (0 == allowedMethods.length) {
			throw new RuntimeException("Can't respond Method not allowed without returning allowed methods");
		}
		StringBuilder allowedMethodsBuilder = new StringBuilder();
		boolean first = true;
		for (String allowedMethod : allowedMethods) {
			if (first) {
				first = false;
			} else {
				allowedMethodsBuilder.append(", ");
			}
			allowedMethodsBuilder.append(allowedMethod);
		}
		String concatenatedAllowedMethods = allowedMethodsBuilder.toString();
		Response response = Response.status(405)
				.entity("405 Method Not Allowed. Allowed: " + concatenatedAllowedMethods + ".")
				.type(MediaType.TEXT_PLAIN).header("Allow", concatenatedAllowedMethods).build();
		return response;
	}

}
