package com.king.server.session;

import java.util.HashMap;
import java.util.Map;

import com.king.server.config.ServerConfig;
import com.king.server.models.Session;
import com.king.server.util.CryptoUtils;

public final class SessionManager {

	private static final long SESSION_EXPIRY = 600000;	//10 minutes
	private static ThreadLocal<Map<String, Session>> sessions = new ThreadLocal<Map<String, Session>>() {
		 @Override 
		 protected Map<String, Session> initialValue() {
		        return new HashMap<String, Session>();
		    }
	};
	
	public static void persist(String sessionId, Session session) {
		Map<String, Session> map = sessions.get();
		map.put(sessionId, session);
	}
	
	public static Session generate(String userId, long nonce) {
		String salt = ServerConfig.getSalt();
		String token = userId + nonce + salt;
		String sessionId =  CryptoUtils.generateMD5(token);
		Session session = new Session(sessionId, userId, nonce);
		return session;
	}
	
	public static boolean isValid(String sessionId) {
		Map<String, Session> map = sessions.get();
		if (map.containsKey(sessionId)) {
			Session session = map.get(sessionId);
			long now = System.currentTimeMillis();
			if (session.hasExpired(now, SESSION_EXPIRY)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static String getUserId(String sessionId) {
		Map<String, Session> map = sessions.get();
		if (map.containsKey(sessionId)) {
			return map.get(sessionId).getUserId();
		}
		return "";
	}
}
