package com.sarzhinskiy.twitter.jbdc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class TwitterConnectionPool implements ConnectionPool {
	private Semaphore available;
	private ConcurrentHashMap<Connectable, Boolean> connectableMap; // if available then boolean is true
	
	private int poolSize;
	private String dbDriver;
	private String connectionUrl;
	private String dbUser;
	private String dbPassword;
	
	private static final int TIME_TRY_CONNECT = 1000;

	public TwitterConnectionPool() {
	}
		
	public TwitterConnectionPool(int poolSize, String dbDriver, String connectionUrl, String dbUser, String dbPassword) {
		this.poolSize = poolSize;
		this.dbDriver = dbDriver;
		this.connectionUrl = connectionUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}
	
	public void init() {
		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException exc) {
			exc.printStackTrace();
		}
		available = new Semaphore(poolSize, true);
		connectableMap = new ConcurrentHashMap<Connectable, Boolean>(poolSize);
		for (int key = 0; key < poolSize; key++) {
			connectableMap.put(getConnectable(key), true);
		}
	}
	
	public void close() {
		for (Connectable connectable: connectableMap.keySet()) {
			connectable.closeConnection();
		}
		connectableMap = null;
		available = null;
	}

	@Override
	public Connectable getConnectable() throws TimeoutException {
		try {
			if (available.tryAcquire(TIME_TRY_CONNECT, TimeUnit.MILLISECONDS)) {
				synchronized(connectableMap) { //? probably don't need in synchronization
					return getAvailableConnectable();
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		throw new TimeoutException("There are no available connections");
	}
	
	private Connectable getAvailableConnectable() {
		for (Connectable connectable: connectableMap.keySet()) {
			if (connectableMap.get(connectable)) {
				connectableMap.put(connectable, false);
				return connectable;
			}
		}
		return null;
	}
	
	@Override
	public void closeConnection(Connectable connectable) {
		synchronized(connectable) {
			if (!connectableMap.get(connectable)) {
				connectableMap.put(connectable, true);
				available.release();
			}
		}
	}
	
	private Connectable getConnectable(int key) {
		return new TwitterConnection(key, connectionUrl, dbUser, dbPassword);
	}
	
	public int getPoolSize() {
		return poolSize;
	}
	
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	
}
