package com.king.server.controllers.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.king.server.controllers.score.UserScore.UserScoreComparator;

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
		if (scoresMap.containsKey(levelId)) {
			PriorityQueue<UserScore> scores = scoresMap.get(levelId);
			if (scores.size() < MAX_SCORES) {
				scores.add(new UserScore(userId, score));
			}
			else {
				UserScore lowestScore = scores.peek();
				if (score > lowestScore.getScore()) {
					scores.poll();
					scores.add(new UserScore(userId, score));
				}
			}
		}
		else {
			scoresMap.put(levelId, new PriorityQueue<UserScore>(MAX_SCORES, new UserScoreComparator()));
		}
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
				fetchedScores.add(score);
			}
			Collections.reverse(fetchedScores);
			for (UserScore s : fetchedScores) {
				sb.append(s.getUserId());
				sb.append("=");
				sb.append(s.getScore());
				sb.append(",");
			}
			//sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		else {
			return "";
		}
	}
}
