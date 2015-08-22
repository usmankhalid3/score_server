package com.king.server.session;

import java.util.HashMap;
import java.util.Map;

import com.king.server.config.ServerConfig;
import com.king.server.models.Session;
import com.king.server.util.CryptoUtils;

public final class SessionManager {

	private static SessionManager instance;
	private static long SESSION_EXPIRY = 6 * 1000;	//10 minutes
	private int cleanUpPeriod = 8 * 1000; // every 12 minutes
	private volatile Map<String, Session> sessions;
	private long lastCleanedAt;
	
	private SessionManager() {
		sessions = new HashMap<String, Session>();
		lastCleanedAt = System.currentTimeMillis();
	}
	
	public static SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}
	
	public void setExpiry(int expiry) {
		SESSION_EXPIRY = expiry;
	}
	
	public void persist(String sessionId, Session session) {
		sessions.put(sessionId, session);
	}
	
	public Session generate(String userId) {
		String salt = ServerConfig.getSalt();
		long nonce = System.currentTimeMillis();
		String token = userId + nonce + salt;
		String sessionId =  CryptoUtils.generateMD5(token);
		Session session = new Session(sessionId, userId, nonce);
		return session;
	}
	
	public Session validateSession(String sessionId) {
		if (sessions.containsKey(sessionId)) {
			Session session = sessions.get(sessionId);
			long now = System.currentTimeMillis();
			if (session.hasExpired(now, SESSION_EXPIRY)) {
				return null;
			}
			if (now - lastCleanedAt > cleanUpPeriod) {
				doCleanup();
			}
			return session;
		}
		return null;
	}
	
	private void doCleanup() {
		System.out.println("Cleaning up old sessions..");
		long now = System.currentTimeMillis();
		lastCleanedAt = now;
		for (String sessionId : sessions.keySet()) {
			Session session = sessions.get(sessionId);
			if (session.hasExpired(now, SESSION_EXPIRY)) {
				sessions.remove(sessionId);
			}
		}
	}
}
