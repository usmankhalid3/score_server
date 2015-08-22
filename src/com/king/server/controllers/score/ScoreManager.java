package com.king.server.controllers.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.king.server.models.UserScore;
import com.king.server.models.UserScore.UserScoreComparator;

public class ScoreManager {

	private static final int MAX_SCORES = 15;
	
	private static ThreadLocal<Map<String, PriorityQueue<UserScore>>> levelScores = new ThreadLocal<Map<String, PriorityQueue<UserScore>>>() {
		 @Override 
		 protected Map<String, PriorityQueue<UserScore>> initialValue() {
			 return new HashMap<String, PriorityQueue<UserScore>>();
		 }
	};
	
	public static void add(String levelId, String userId, Integer score) {
		Map<String, PriorityQueue<UserScore>> scoresMap = levelScores.get();
		UserScore newScore = new UserScore(userId, score);
		if (scoresMap.containsKey(levelId)) {
			PriorityQueue<UserScore> scores = scoresMap.get(levelId);
			if (scores.size() < MAX_SCORES) {
				addScore(scores, newScore);
			}
			else {
				UserScore lowestScore = scores.peek();
				if (!scores.contains(newScore) && score > lowestScore.getScore()) {
					scores.poll();
					addScore(scores, newScore);
				}
			}
		}
		else {
			PriorityQueue<UserScore> scores = new PriorityQueue<UserScore>(MAX_SCORES, new UserScoreComparator());
			scores.add(new UserScore(userId, score));
			scoresMap.put(levelId, scores);
		}
	}
	
	private static void addScore(PriorityQueue<UserScore> scores, UserScore newScore) {
		for (UserScore score : scores) {
			if (score.getUserId().equals(newScore.getUserId())) {
				scores.remove(score);
				break;
			}
		}
		scores.add(newScore);
	}
	
	public static String getHighScores(String levelId) {
		StringBuilder sb = new StringBuilder();
		List<UserScore> fetchedScores = new ArrayList<UserScore>();
		Map<String, PriorityQueue<UserScore>> scoresMap = levelScores.get();
		if (scoresMap.containsKey(levelId)) {
			PriorityQueue<UserScore> scores = scoresMap.get(levelId);
			PriorityQueue<UserScore> highScores = new PriorityQueue<UserScore>(scores);
			while (!highScores.isEmpty()) {
				UserScore score = highScores.poll();
				if (score != null) {
					fetchedScores.add(score);
				}
			}
			Collections.reverse(fetchedScores);
			for (UserScore s : fetchedScores) {
				sb.append(s.getUserId());
				sb.append("=");
				sb.append(s.getScore());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		else {
			return "";
		}
	}
}
