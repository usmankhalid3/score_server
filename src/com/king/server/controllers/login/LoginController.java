package com.king.server.controllers.login;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.king.server.controllers.common.BaseController;
import com.king.server.models.Session;
import com.king.server.session.SessionManager;

public class LoginController extends BaseController {
	
	@Override
	public void handleGET(Map<String, String> params) throws IOException {
		String userId = params.get(PARAM_ID);
		long nonce = System.currentTimeMillis();
		Session session = SessionManager.generate(userId, nonce);
		SessionManager.persist(session.getSessionId(), session);
		success(session.getSessionId());
	}
	
	@Override
	public void handlePOST(Map<String, String> params, InputStream body) throws IOException {
		error("POST not supported");
	}

}
