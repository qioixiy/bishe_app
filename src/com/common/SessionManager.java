package com.common;

public class SessionManager {
	private static SessionManager sm = null;
	private String token = null;

	public static SessionManager GetInstance() {
		if (sm == null) {
			sm = new SessionManager();
		}

		return sm;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}
}
