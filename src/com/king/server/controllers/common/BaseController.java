package com.king.server.controllers.common;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.king.server.util.HttpStatusCode;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public abstract class BaseController implements SimpleServer {

	protected ThreadLocal<HttpExchange> exchange = new ThreadLocal<HttpExchange>();
	private static final String METHOD_GET  = "GET";
	private static final String METHOD_POST = "POST";
	private static final String PARAM_DELIM = "&";
	private static final String VALUE_DELIM = "=";
	protected static final String PARAM_ID = "contextid";
	protected static final String PARAM_SESSION = "sessionkey";
	
	public void handle(HttpExchange exchange) throws IOException {
		this.exchange.set(exchange);
		String method = exchange.getRequestMethod();
		URI uri = exchange.getRequestURI();
		Map<String, String> params = getParams(uri);
		params.put(PARAM_ID, (String)exchange.getAttribute(PARAM_ID));
		if (isGet(method)) {
			handleGET(params);
		}
		else if (isPost(method)) {
			handlePOST(params, exchange.getRequestBody());
		}
	}
	
	protected void success(String resp) throws IOException {
		exchange.get().sendResponseHeaders(HttpStatusCode.OK, resp.getBytes().length);
		writeResponse(resp);
	}
	
	protected void error(String resp) throws IOException {
		error(HttpStatusCode.SERVER_ERROR, resp);
	}
	
	protected void error(int errorCode, String resp) throws IOException {
		exchange.get().sendResponseHeaders(errorCode, resp.getBytes().length);
		writeResponse(resp);
	}
	
	private void writeResponse(String text) throws IOException {
		Headers headers = exchange.get().getResponseHeaders();
		headers.set("Content-Type", "text/html");
		if (text != null) {
			OutputStream os = exchange.get().getResponseBody();
			os.write(text.getBytes());
			os.close();
		}
	}
	
	private Map<String, String> getParams(URI uri) {
		Map<String, String> params = new HashMap<String, String>();
		String query = uri.getQuery();
		if (query != null) {
			String[] queryParams = query.split(PARAM_DELIM);
			for (String param : queryParams) {
				String[] parts = param.split(VALUE_DELIM);
				if (parts != null && parts.length > 1) {
					params.put(parts[0], parts[1]);
				}
			}
		}
		return params;
	}
	
	private boolean isGet(String method) {
		return METHOD_GET.equalsIgnoreCase(method);
	}
	
	private boolean isPost(String method) {
		return METHOD_POST.equalsIgnoreCase(method);
	}
}
