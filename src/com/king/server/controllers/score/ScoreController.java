package com.king.server.controllers.score;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import com.king.server.controllers.common.BaseController;
import com.king.server.session.SessionManager;

public class ScoreController extends BaseController {

	public void handleGET(Map<String, String> params) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void handlePOST(Map<String, String> params, InputStream body) throws IOException {
		String sessionId = params.get(PARAM_SESSION);
		if (sessionId != null) {
			boolean isValid = SessionManager.isValid(sessionId);
			if (isValid) {
				//TODO post scores
				String userId = SessionManager.getUserId(sessionId);
				String levelId = params.get(PARAM_LEVEL);
				Integer score = getScoreFromBody(body);
				if (score == -1) {
					error("Score is missing");
				}
				else {
					ScoreManager.add(levelId, userId, score);
					success("score added!");
				}
			}
			else {
				error("Invalid session");
			}
		}
		else {
			error("sessionkey is required");
		}
	}
	
	private Integer getScoreFromBody(InputStream body) {
		if (body != null) {
			Scanner scanner = new Scanner(body,"UTF-8");
			String score = scanner.useDelimiter("\\A").next();
			scanner.close();
			return Integer.parseInt(score);
		}
		else {
			return -1;
		}
	}

}
