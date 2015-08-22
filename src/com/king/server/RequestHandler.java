package com.king.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.king.server.controllers.common.BaseController;
import com.king.server.controllers.login.LoginController;
import com.king.server.controllers.score.ScoreController;
import com.king.server.util.HttpStatusCode;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {

	private static final String PATH_DELIM = "/";
	private static final String ID_PARAM = "contextid";
	
	public void handle(HttpExchange exchange) throws IOException {
		String method = exchange.getRequestMethod();
		URI uri = exchange.getRequestURI();
		System.out.println("Incoming request: " + method + " - " + uri);
		String[] uriParts = parseURI(uri);
		if (uriParts != null && uriParts.length > 1) {
			exchange.setAttribute(ID_PARAM, uriParts[0]);
			routeRequest(uriParts[1], exchange);
		}
		else {
			error(exchange, "Missing an id, could not route the request");
		}
	}
	
	private void routeRequest(String target, HttpExchange exchange) throws IOException {
		System.out.println("Routing request to: " + target);
		BaseController controller;
		switch(target) {
			case "login": controller = new LoginController(); break;
			case "score": controller = new ScoreController(); break;
			case "highscorelist": controller = new ScoreController(); break;
			default: controller = null;
		}
		if (controller != null) {
			controller.handle(exchange);
		}
		else {
			error(exchange, "Target unresolvable, could not route the request");
		}
	}
	
	private void error(HttpExchange exchange, String resp) throws IOException {
		exchange.sendResponseHeaders(HttpStatusCode.ERROR, resp.getBytes().length);
		Headers headers = exchange.getResponseHeaders();
		headers.set("Content-Type", "text/html");
		OutputStream os = exchange.getResponseBody();
		os.write(resp.getBytes());
		os.close();
	}
	
	private String[] parseURI(URI uri) {
		String path = uri.getPath();
		if (path != null) {
			String[] splitted = path.split(PATH_DELIM);
			int discard = 0;
			for (int i = 0; i < splitted.length; i++) {
				if (splitted[i].isEmpty()) {
					discard++;
				}
			}
			int pIndex = 0;
			String[] parts = new String[splitted.length - discard];
			for (int i = 0; i < splitted.length; i++) {
				if (!splitted[i].isEmpty()) {
					parts[pIndex] = splitted[i];
					pIndex++;
				}
			}
			return parts;
		}
		return null;
	}
}
