package com.king.server.test;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Test;

import com.king.server.controllers.score.ScoreManager;
import com.king.server.models.UserScore;
import com.king.server.models.UserScore.UserScoreComparator;

public class ScoreTest extends TestCase {

	@Test
	public void testScoreAdd() {
		String levelId = "1";
		String userId = "45";
		Integer score = 10;
		ScoreManager.getInstance().add(levelId, userId, score);
		String highScores = ScoreManager.getInstance().getHighScores(levelId);
		assertEquals(highScores, "45=10");
	}
	
	@Test
	public void testTop15() {
		String levelId = "1";
		UserScore[] scores = new UserScore[20];
		for (int i = 0; i < scores.length; i++) {
			String userId = i + "";
			Integer score = (int)(Math.random() * 1000) % 500;
			scores[i] = new UserScore(userId, score);
		}
		
		UserScore[] highestScores = scores.clone();
		Arrays.sort(highestScores, Collections.reverseOrder(new UserScoreComparator()));
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 15; i++) {
			UserScore score = highestScores[i];
			sb.append(score.getUserId());
			sb.append("=");
			sb.append(score.getScore());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		String expectedString = sb.toString();
		
		for (int i = 0; i < scores.length; i++) {
			UserScore score = scores[i];
			ScoreManager.getInstance().add(levelId, score.getUserId(), score.getScore());
		}
		String highScores = ScoreManager.getInstance().getHighScores(levelId);
		assertEquals(highScores, expectedString);
	}

}
