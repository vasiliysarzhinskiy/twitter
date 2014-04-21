package com.sarzhinskiy.twitter.jbdc;

import java.util.concurrent.TimeoutException;

public interface ConnectionPool {
	public Connectable getConnectable() throws TimeoutException;
	public void closeConnection(Connectable connection);
}
