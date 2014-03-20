package com.sap.research.fiware.ngsi10.test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public enum HttpRequestBuilder {

	GET("GET"),
	PUT("PUT"),
	POST("POST"),
	DELETE("DELETE");

	private String method = null;
	private URL url = null;
	private String contentType = null;
	private String body = null;
	private HttpURLConnection connection = null;

	private HttpRequestBuilder(String method) {
		this.method = method;
	}

	public HttpRequestBuilder url(String url) throws MalformedURLException {
		this.url = new URL(url);
		return this;
	}

	public HttpRequestBuilder contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public HttpRequestBuilder body(String body) {
		this.body = body;
		return this;
	}

	public HttpURLConnection build() throws IOException {
		checkPreconditions();
		createConnection();
		setMethod();
		setContentType();
		setBody();
		return connection;
	}

	private void checkPreconditions() {
		if (null == url) {
			throw new RuntimeException("url is not set");
		}
	}

	private void createConnection() throws IOException {
		connection = (HttpURLConnection) url.openConnection();
	}

	private void setMethod() throws ProtocolException {
		connection.setRequestMethod(method);
	}

	private void setContentType() {
		if (null != contentType) {
			connection.setRequestProperty("Content-Type", contentType);
		}
	}

	private void setBody() throws IOException {
		if (null == body) {
			return;
		}
		connection.setDoOutput(true);
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write(body);
		out.close();
	}

}
