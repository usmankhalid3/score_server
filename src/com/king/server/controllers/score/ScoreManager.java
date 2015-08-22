package com.king.server.controllers.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.king.server.models.UserScore;
import com.king.server.models.UserScore.UserScoreComparator;

public final class ScoreManager {

	private static ScoreManager instance = new ScoreManager();
	private static final int MAX_SCORES = 15;
	private static ConcurrentMap<String, PriorityQueue<UserScore>> levels = new ConcurrentHashMap<String, PriorityQueue<UserScore>>();
	
	private ScoreManager() {
	}
	
	public static ScoreManager getInstance() {
		return instance;
	}
	
	public void add(String levelId, String userId, Integer score) {
		UserScore newScore = new UserScore(userId, score);
		if (levels.containsKey(levelId)) {
			PriorityQueue<UserScore> scores = levels.get(levelId);
			synchronized (scores) {
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
		}
		else {
			PriorityQueue<UserScore> scores = new PriorityQueue<UserScore>(MAX_SCORES, new UserScoreComparator());
			scores.add(new UserScore(userId, score));
			levels.put(levelId, scores);
		}
	}
	
	private void addScore(PriorityQueue<UserScore> scores, UserScore newScore) {
		for (UserScore score : scores) {
			if (score.getUserId().equals(newScore.getUserId())) {
				scores.remove(score);
				break;
			}
		}
		scores.add(newScore);
	}
	
	public String getHighScores(String levelId) {
		if (levels.containsKey(levelId)) {
			ArrayList<UserScore> fetchedScores = fetchScores(levelId);
			return buildResponse(fetchedScores);
		}
		else {
			return "";
		}	
	}
	
	private String buildResponse(ArrayList<UserScore> scores) {
		StringBuilder sb = new StringBuilder();
		if (!scores.isEmpty()) {
			for (UserScore s : scores) {
				sb.append(s.getUserId());
				sb.append("=");
				sb.append(s.getScore());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();	
	}
	
	private ArrayList<UserScore> fetchScores(String levelId) {
		ArrayList<UserScore> fetchedScores = new ArrayList<UserScore>();
		PriorityQueue<UserScore> scores = levels.get(levelId);
		synchronized(scores) {
			if (!scores.isEmpty()) {
				PriorityQueue<UserScore> highScores = new PriorityQueue<UserScore>(scores);
				while (!highScores.isEmpty()) {
					UserScore score = highScores.poll();
					if (score != null) {
						fetchedScores.add(score);
					}
				}
				Collections.reverse(fetchedScores);
			}
		}
		return fetchedScores;
	}
	
	public void deleteAllScores() {
		levels = new ConcurrentHashMap<String, PriorityQueue<UserScore>>();
	}
}
