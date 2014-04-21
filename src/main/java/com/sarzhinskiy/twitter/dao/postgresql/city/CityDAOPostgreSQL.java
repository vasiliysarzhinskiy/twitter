package com.sarzhinskiy.twitter.dao.postgresql.city;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;




import com.sarzhinskiy.twitter.bean.city.City;
import com.sarzhinskiy.twitter.dao.CityDAO;
import com.sarzhinskiy.twitter.jbdc.Connectable;
import com.sarzhinskiy.twitter.jbdc.ConnectionPool;

import static com.sarzhinskiy.twitter.dao.postgresql.UtilDAOPostgreSQL.*;

public class CityDAOPostgreSQL implements CityDAO {

	private ConnectionPool pool;
	
	private static final String FIELD_ID = "id";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_COUNTRY_ID = "country_id";
	private static final String FIELD_STATUS = "status";
	
	private static final String QUERY_CREATE_CITY = "INSERT INTO city (name, country_id, status) VALUES (?, (SELECT id FROM country  WHERE name=?), ?)";
	private static final String QUERY_UPDATE_CITY = "UPDATE city  SET name=?, country_id=(SELECT id FROM country WHERE name=?), status=? WHERE id=?";
	private static final String QUERY_SELECT_CITY_BY_ID = "SELECT city.*, country.name as countryName FROM city INNER JOIN country ON city.country_id=country.id WHERE city.id=?";
	private static final String QUERY_SELECT_CITY_BY_NAME = "SELECT city.*, country.name as countryName FROM city INNER JOIN country ON city.country_id=country.id WHERE city.name=?";
	private static final String QUERY_SELECT_ALL = "SELECT city.*, country.name as countryName FROM city INNER JOIN country ON city.country_id=country.id";
	private static final String QUERY_SELECT_CITY_BY_COUNTRY = "SELECT city.*, country.name as countryName FROM city INNER JOIN country ON city.country_id=country.id WHERE country.name=?";
	private static final String QUERY_DELETE_BY_ID = "DELETE FROM city WHERE id=?";
	private static final String QUERY_DELETE_BY_NAME = "DELETE FROM city WHERE name=?";
	private static final String QUERY_DELETE_BY_NAME_AND_COUNTRY = "DELETE FROM city WHERE name=? AND country_id=(SELECT id FROM country WHERE name=?)";
	private static final String QUERY_DELETE_ALL = "DELETE FROM city";
	
	
	public CityDAOPostgreSQL(ConnectionPool pool) {
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
	public boolean create(City city) {
		int updateNum = 0;
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_CREATE_CITY, Statement.RETURN_GENERATED_KEYS);
			prepStatement.setString(1, city.getName());
			prepStatement.setString(2, city.getCountry());
			prepStatement.setInt(3, city.getStatus());
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Integer generatedId = resultSet.getInt(FIELD_ID);
				city.setId(generatedId);
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
	public boolean update(City city) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_CITY);
			prepStatement.setString(1, city.getName());
			prepStatement.setString(2, city.getCountry());
			prepStatement.setInt(3, city.getStatus());
			prepStatement.setInt(4, city.getId());
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

	private City map(ResultSet resultSet) throws SQLException {
		Integer id = resultSet.getInt(FIELD_ID);
		String name = resultSet.getString(FIELD_NAME);
		Integer status = resultSet.getInt(FIELD_STATUS);
		String country = resultSet.getString("countryName");
	
		City city = new City(id, name, country);
		city.setStatus(status);
		return city;
	}
	
	@Override
	public City findById(Integer id) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		City city = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_CITY_BY_ID);
			prepStatement.setInt(1, id);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				city = map(resultSet);
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
		return city;
	}

	@Override
	public List<City> findByName(String name) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<City> cities = new ArrayList<City>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_CITY_BY_NAME);
			prepStatement.setString(1, name);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				City city = map(resultSet);
				cities.add(city);
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
		return cities;
	}

	@Override
	public List<City> findByCountry(String country) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<City> cities = new ArrayList<City>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_CITY_BY_COUNTRY);
			prepStatement.setString(1, country);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				City city = map(resultSet);
				cities.add(city);
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
		return cities;
	}

	@Override
	public List<City> findAll() {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<City> cities = new ArrayList<City>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_ALL);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				City city = map(resultSet);
				cities.add(city);
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
		return cities;
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
	public boolean remove(String cityName, String countryName) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_BY_NAME_AND_COUNTRY);
			prepStatement.setString(1, cityName);
			prepStatement.setString(2, countryName);
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
