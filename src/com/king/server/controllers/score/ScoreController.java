package com.king.server.controllers.score;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import com.king.server.controllers.common.BaseController;
import com.king.server.session.SessionManager;

public class ScoreController extends BaseController {

	public void handleGET(final Map<String, String> params) throws IOException {
		String levelId = params.get(PARAM_ID);
		if (levelId != null) {
			String highscores = ScoreManager.getHighScores(levelId);
			success(highscores);
		}
		else {
			error("levelid is required");
		}
	}

	public void handlePOST(final Map<String, String> params, InputStream body) throws IOException {
		String levelId = params.get(PARAM_ID);
		if (levelId != null) {
			String sessionId = params.get(PARAM_SESSION);
			if (sessionId != null) {
				boolean isValid = SessionManager.isValid(sessionId);
				if (isValid) {
					String userId = SessionManager.getUserId(sessionId);
					Integer score = getScoreFromBody(body);
					if (score == -1) {
						error("Score is required");
					}
					else {
						ScoreManager.add(levelId, userId, score);
						success("Score added!");
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
		else {
			error("levelid is required");
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
