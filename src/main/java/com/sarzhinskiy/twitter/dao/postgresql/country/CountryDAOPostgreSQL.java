package com.sarzhinskiy.twitter.dao.postgresql.country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;



import com.sarzhinskiy.twitter.bean.country.Country;
import com.sarzhinskiy.twitter.dao.CountryDAO;
import com.sarzhinskiy.twitter.jbdc.Connectable;
import com.sarzhinskiy.twitter.jbdc.ConnectionPool;

import static com.sarzhinskiy.twitter.dao.postgresql.UtilDAOPostgreSQL.*;

public class CountryDAOPostgreSQL implements CountryDAO {

	private ConnectionPool pool;
	
	private static final String FIELD_ID = "id";
	private static final String FIELD_NAME = "name";
	
	private static final String QUERY_CREATE_COUNTRY = "INSERT INTO country (name) VALUES (?)";
	private static final String QUERY_SELECT_COUNTRY_BY_ID = "SELECT * FROM country WHERE id = ?";
	private static final String QUERY_SELECT_COUNTRY_BY_NAME = "SELECT * FROM country WHERE name = ?";
	private static final String QUERY_SELECT_ALL = "SELECT * FROM country";
	private static final String QUERY_UPDATE_COUNTRY = "UPDATE country SET name=? WHERE id = ?";
	private static final String QUERY_DELETE_BY_ID = "DELETE FROM country WHERE id=?";
	private static final String QUERY_DELETE_BY_NAME = "DELETE FROM country WHERE name=?";
	private static final String QUERY_DELETE_ALL = "DELETE FROM country";
	
	public CountryDAOPostgreSQL(ConnectionPool pool) {
		this.pool = pool;
	}
	
	private Connectable getConnectable() {
		try {
			return pool.getConnectable();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Connection getConnection(Connectable connectable) {
		return connectable.getConnection();
	}
	
	private void closeConnection(Connectable connectable) {
		pool.closeConnection(connectable);
	}
	
	@Override
	public boolean create(Country country) {
		int updateNum = 0;
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_CREATE_COUNTRY, Statement.RETURN_GENERATED_KEYS);
			prepStatement.setString(1, country.getName());
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Integer generatedId = resultSet.getInt(FIELD_ID);
				country.setId(generatedId);
			}
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(resultSet);
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	@Override
	public Country findById(Integer id) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Country country = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_COUNTRY_BY_ID);
			prepStatement.setInt(1, id);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				country = map(resultSet);
			}
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(resultSet);
			close(prepStatement);
			closeConnection(connectable);
		}
		return country;
	}

	@Override
	public Country findByName(String name) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Country country = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_COUNTRY_BY_NAME);
			prepStatement.setString(1, name);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				country = map(resultSet);
			}
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(resultSet);
			close(prepStatement);
			closeConnection(connectable);
		}
		return country;
	}

	@Override
	public List<Country> findAll() {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<Country> countries = new ArrayList<Country>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_ALL);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				Country country = map(resultSet);
				countries.add(country);
			}
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(resultSet);
			close(prepStatement);
			closeConnection(connectable);
		}
		return countries;
	}
	
	@Override
	public boolean update(Country country) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_COUNTRY);
			prepStatement.setString(1, country.getName());
			prepStatement.setInt(2, country.getId());
			updateNum = prepStatement.executeUpdate();
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	private Country map(ResultSet resultSet) throws SQLException {
		Integer id = resultSet.getInt(FIELD_ID);
		String name = resultSet.getString(FIELD_NAME);
		Country country = new Country(id, name);
		return country;
	}
	
	@Override
	public boolean remove(Integer id) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_BY_ID);
			prepStatement.setInt(1, id);
			updateNum = prepStatement.executeUpdate();
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);		
	}
	
	@Override
	public boolean remove(String name) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_BY_NAME);
			prepStatement.setString(1, name);
			updateNum = prepStatement.executeUpdate();
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);		
	}
	
	@Override
	public boolean removeAll() {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_ALL);
			updateNum = prepStatement.executeUpdate();
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
		finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);	
	}

}
