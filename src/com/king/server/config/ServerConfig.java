package com.king.server.config;

public class ServerConfig {

	private static int port = 1435;
	private static String salt = "ERJGBK325498TFHRJV3ROUT43GFWV";
	private static int poolSize = 50;
	
	public static int getPort() {
		return port;
	}
	
	public static String getSalt() {
		return salt;
	}
	
	public static int getPoolSize() {
		return poolSize;
	}
}
