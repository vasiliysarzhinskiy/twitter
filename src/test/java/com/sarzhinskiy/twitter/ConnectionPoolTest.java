package com.sarzhinskiy.twitter;


import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;

import com.sarzhinskiy.twitter.jbdc.Connectable;
import com.sarzhinskiy.twitter.jbdc.ConnectionPool;

public class ConnectionPoolTest implements ConnectionPool {

	private DataSource dataSource;
	
	public ConnectionPoolTest() {
	}
	
	public ConnectionPoolTest(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connectable getConnectable() throws TimeoutException {
		return new ConnectionTest(dataSource);
	}

	@Override
	public void closeConnection(Connectable connection) {
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
