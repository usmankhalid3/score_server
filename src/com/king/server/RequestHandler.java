package com.king.server;

import java.io.IOException;
import java.net.URI;

import com.king.server.controllers.common.BaseController;
import com.king.server.controllers.login.LoginController;
import com.king.server.controllers.score.ScoreController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {

	private static final String PATH_DELIM = "/";
	private static final String ID_PARAM = "contextid";
	
	public void handle(HttpExchange exchange) throws IOException {
		URI uri = exchange.getRequestURI();
		System.out.println("Incoming request: " + uri);
		String[] uriParts = parseURI(uri);
		if (uriParts != null && uriParts.length > 1) {
			exchange.setAttribute(ID_PARAM, uriParts[1]);
			routeRequest(uriParts[2], exchange);
		}
	}
	
	private void routeRequest(String target, HttpExchange exchange) throws IOException {
		System.out.println("Routing request to: " + target);
		BaseController controller;
		switch(target) {
			case "login": controller = new LoginController(); break;
			case "score": controller = new ScoreController(); break;
			case "highscore": controller = new ScoreController(); break;
			default: controller = null;
		}
		if (controller != null) {
			controller.handle(exchange);
		}
		else {
			//TODO handle this case
		}
	}
	
	private String[] parseURI(URI uri) {
		String path = uri.getPath();
		if (path != null) {
			String[] parts = path.split(PATH_DELIM);
			return parts;
		}
		return null;
	}
}
