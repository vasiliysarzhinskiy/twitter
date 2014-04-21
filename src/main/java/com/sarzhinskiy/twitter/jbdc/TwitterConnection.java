package com.sarzhinskiy.twitter.jbdc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TwitterConnection implements Connectable {
	private final int id;
	private final Connection connection;
	
	public TwitterConnection(int id, String connectionUrl, String dbUser, String dbPassword) {
		this.id = id;
		connection = initConnection(connectionUrl, dbUser, dbPassword);
	}
	
	private Connection initConnection(String connectionUrl, String dbUser, String dbPassword) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return connection;
	}
	
	@Override
	public Connection getConnection() {
		return connection;
	}
	
	@Override
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		}
	}

	public int getID() {
		return id;
	}
}
