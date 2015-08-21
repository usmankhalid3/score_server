package com.king.server.controllers.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface SimpleServer {

	public void handleGET(Map<String, String> params) throws IOException;
	public void handlePOST(Map<String, String> params, InputStream body) throws IOException;
}
