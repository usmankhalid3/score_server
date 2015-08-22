package com.king.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.king.server.config.ServerConfig;
import com.sun.net.httpserver.HttpServer;

public class Server {
	
	private HttpServer server;
	private int port;

	public Server(int port) {
		try {
			this.port = port;
			initServer();
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to init the server: " + e.getMessage());
		}
	}
	
	private void initServer() throws IOException {
		this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
		this.server.createContext("/", new RequestHandler());
		this.server.setExecutor(Executors.newFixedThreadPool(ServerConfig.getPoolSize()));
	}
	
	public void start() {
		this.server.start();
		 System.out.println("Server running on port: " + this.port );
	}
	
	public static void main(String[] args) {
		Server server = new Server(ServerConfig.getPort());
		server.start();
	}
}
