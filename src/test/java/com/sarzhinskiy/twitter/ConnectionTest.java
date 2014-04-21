package com.sarzhinskiy.twitter;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.sarzhinskiy.twitter.jbdc.Connectable;

public class ConnectionTest implements Connectable {
	private Connection connection;
	
	public ConnectionTest(DataSource dataSource) {
		connection = initConnection(dataSource);
	}
	
	private Connection initConnection(DataSource dataSource) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} 
		catch (SQLException exc) {
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
	}

}
