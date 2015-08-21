package com.king.server.controllers.score;

import java.util.Comparator;

public class UserScore {

	private String userId;
	private Integer score;
	
	public UserScore(String userId, Integer score) {
		super();
		this.userId = userId;
		this.score = score;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public static class UserScoreComparator implements Comparator<UserScore> {

		@Override
		public int compare(UserScore o1, UserScore o2) {
			return o1.getScore().compareTo(o2.getScore());
		}
		
	}
}
