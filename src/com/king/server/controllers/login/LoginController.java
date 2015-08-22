package com.king.server.controllers.login;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.king.server.controllers.common.BaseController;
import com.king.server.models.Session;
import com.king.server.session.SessionManager;
import com.king.server.util.HttpStatusCode;

public class LoginController extends BaseController {
	
	@Override
	public void handleGET(Map<String, String> params) throws IOException {
		String userId = params.get(PARAM_ID);
		Session session = SessionManager.getInstance().generate(userId);
		if (session != null) {
			SessionManager.getInstance().persist(session.getSessionId(), session);
			success(session.getSessionId());
		}
		else {
			error("Failed to generate a session");
		}
	}
	
	@Override
	public void handlePOST(Map<String, String> params, InputStream body) throws IOException {
		error(HttpStatusCode.TARGET_UNRESOLVABLE, "POST not supported");
	}

}
