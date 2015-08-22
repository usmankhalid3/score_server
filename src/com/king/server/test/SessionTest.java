package com.king.server.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.king.server.models.Session;
import com.king.server.session.SessionManager;

public class SessionTest extends TestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		SessionManager.getInstance().setExpiry(2000);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		SessionManager.getInstance().setExpiry(10*60*1000);
	}
	
	@Test
	public void testCreateSessionId() {
		String userId = "45";
		Session session = SessionManager.getInstance().generate(userId);
		String sessionId = session.getSessionId();
		assertEquals(true, sessionId.matches("[0-9a-z]+"));
	}
	
	@Test
	public void testSessionExpiry() throws InterruptedException  {
		String userId = "45";
		Session session = SessionManager.getInstance().generate(userId);
		String sessionId = session.getSessionId();
		SessionManager.getInstance().persist(sessionId, session);
		assertNotNull(SessionManager.getInstance().validateSession(sessionId));
		Thread.sleep(2000);
		assertNull(SessionManager.getInstance().validateSession(sessionId));
	}

}
