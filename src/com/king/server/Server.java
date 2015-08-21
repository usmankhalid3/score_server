package com.king.server;

import java.io.IOException;
import java.net.InetSocketAddress;

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
			//TODO: Handle this exception gracefully
			throw new RuntimeException(e);
		}
		finally {
			//TODO: handle finally
		}
	}
	
	private void initServer() throws IOException {
		this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
		this.server.createContext("/", new RequestHandler());
		this.server.setExecutor(null);
	}
	
	public void start() {
		this.server.start();
		 System.out.println("Server is listening on port " + this.port );
	}
	
	public static void main(String[] args) {
		Server server = new Server(ServerConfig.getPort());
		server.start();
	}
}
