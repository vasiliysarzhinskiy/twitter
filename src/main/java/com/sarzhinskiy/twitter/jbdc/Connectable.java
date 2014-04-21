package com.sarzhinskiy.twitter.jbdc;

import java.sql.Connection;

public interface Connectable {
	public Connection getConnection();
	public void closeConnection();

}
