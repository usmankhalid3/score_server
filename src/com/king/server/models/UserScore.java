package com.king.server.models;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserScore other = (UserScore) obj;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public static class UserScoreComparator implements Comparator<UserScore> {

		@Override
		public int compare(UserScore o1, UserScore o2) {
			if (o1.getScore().equals(o2.getScore())) {
				// if score is equal do comparison based on userId
				return o1.getUserId().compareTo(o2.getUserId());
			}
			else {
				return o1.getScore().compareTo(o2.getScore());
			}
		}
		
	}
}
