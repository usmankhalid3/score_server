package com.king.server.controllers.score;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import com.king.server.controllers.common.BaseController;
import com.king.server.models.Session;
import com.king.server.session.SessionManager;
import com.king.server.util.HttpStatusCode;

public class ScoreController extends BaseController {

	public void handleGET(final Map<String, String> params) throws IOException {
		String levelId = params.get(PARAM_ID);
		if (levelId != null) {
			String highscores = ScoreManager.getInstance().getHighScores(levelId);
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
				Session session = SessionManager.getInstance().validateSession(sessionId);
				if (session != null) {
					String userId = session.getUserId();
					Integer score = getScoreFromBody(body);
					if (score == -1) {
						error("Score is required");
					}
					else {
						ScoreManager.getInstance().add(levelId, userId, score);
						success("Score saved");
					}
				}
				else {
					error(HttpStatusCode.INVALID_SESSION, "Invalid session");
				}
			}
			else {
				error(HttpStatusCode.INVALID_SESSION, "sessionkey is required");
			}
		}
		else {
			error(HttpStatusCode.TARGET_UNRESOLVABLE, "levelid is required");
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
