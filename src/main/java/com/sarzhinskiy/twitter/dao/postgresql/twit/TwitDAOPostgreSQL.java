package com.sarzhinskiy.twitter.dao.postgresql.twit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

import org.joda.time.DateTime;

import com.sarzhinskiy.twitter.bean.Image;
import com.sarzhinskiy.twitter.bean.twit.Twit;
import com.sarzhinskiy.twitter.dao.TwitDAO;
import com.sarzhinskiy.twitter.jbdc.Connectable;
import com.sarzhinskiy.twitter.jbdc.ConnectionPool;

import static com.sarzhinskiy.twitter.dao.postgresql.UtilDAOPostgreSQL.*;
import static com.sarzhinskiy.twitter.dao.postgresql.twit.TwitDAOPostgreSQLConstants.*;

public class TwitDAOPostgreSQL implements TwitDAO {
	
	private ConnectionPool pool;
	
	public TwitDAOPostgreSQL(ConnectionPool pool) {
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
	public boolean create(Twit twit) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_CREATE_TWIT, Statement.RETURN_GENERATED_KEYS);
			prepStatement.setLong(1, twit.getOwnerId());
			prepStatement.setString(2, twit.getText());
			prepStatement.setTimestamp(3, getTimestampFrom(twit.getDateTime()));
			prepStatement.setInt(4, twit.getLikeNumber());
			prepStatement.setInt(5, twit.getStatus());
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Long generatedID = resultSet.getLong(FIELD_ID);
				twit.setId(generatedID);
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			close(resultSet);
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}
	
	private Twit map(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong(FIELD_ID);
		Long ownerId = resultSet.getLong(FIELD_OWNER_ID);
		String text = resultSet.getString(FIELD_TEXT);
		Timestamp timestamp = resultSet.getTimestamp(FIELD_TIMESTAMP);
		DateTime dateTime = new DateTime(timestamp);
		Integer likeNumber = resultSet.getInt(FIELD_LIKE_NUMBER);
		Integer status = resultSet.getInt(FIELD_STATUS);
		
		Twit twit = new Twit(id, ownerId, text, dateTime);
		twit.setLikeNumber(likeNumber);
		twit.setStatus(status);
//		twit.setImage(image);
		return twit;
	}

	@Override
	public Twit findById(Long id) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		Twit twit = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_BY_ID);
			prepStatement.setLong(1, id);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				twit = map(resultSet);
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
		return twit;
	}	
	
	@Override
	public List<Twit> findAllByOwnerEmail(String ownerEmail) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<Twit> twits = new ArrayList<Twit>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_BY_OWNER_EMAIL);
			prepStatement.setString(1, ownerEmail);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				Twit twit = map(resultSet);
				twits.add(twit);
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
		return twits;
	}

	@Override
	public List<Twit> findAllByOwnerId(Long ownerId) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<Twit> twits = new ArrayList<Twit>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_BY_OWNER_ID);
			prepStatement.setLong(1, ownerId);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				Twit twit = map(resultSet);
				twits.add(twit);
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
		return twits;
	}
	
	@Override
	public List<Twit> findLastByOwnerId(Long ownerId, int numberTwits) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<Twit> twits = new ArrayList<Twit>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_LAST_TWITS_BY_OWNER_ID);
			prepStatement.setLong(1, ownerId);
			prepStatement.setInt(2, numberTwits);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				Twit twit = map(resultSet);
				twits.add(twit);
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
		return twits;	
	}

	@Override
	public boolean update(Twit twit) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_TWIT);
			prepStatement.setLong(1, twit.getOwnerId());
			prepStatement.setString(2, twit.getText());
			prepStatement.setTimestamp(3, getTimestampFrom(twit.getDateTime()));
			prepStatement.setInt(4, twit.getLikeNumber());
			prepStatement.setInt(5, twit.getStatus());
			prepStatement.setLong(6, twit.getId());
			updateNum = prepStatement.executeUpdate();
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	@Override
	public boolean updateLikeNumber(Twit twit) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_LIKE_NUMBER_TWIT);
			prepStatement.setInt(1, twit.getLikeNumber());
			prepStatement.setLong(2, twit.getId());
			updateNum = prepStatement.executeUpdate();
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	@Override
	public boolean remove(Long id) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_BY_ID);
			prepStatement.setLong(1, id);
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

	@Override
	public boolean insertImage(Twit twit, Image image) {
		FileInputStream fis = null;		
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_CREATE_IMAGE);
			File file = image.getImageFile();
			fis = new FileInputStream(file);
			prepStatement.setBinaryStream(1, fis, file.length());
			prepStatement.setLong(2, twit.getId());
			updateNum = prepStatement.executeUpdate();
			if (isNotZero(updateNum)) {
				twit.setImage(image);
			}
		}
		catch (SQLException | FileNotFoundException exc) {
			exc.printStackTrace();
		}
		finally {
			close(fis);
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	
	@Override
	public boolean removeImage(Twit twit) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_IMAGE);
			prepStatement.setLong(1, twit.getId());
			updateNum = prepStatement.executeUpdate();
			if (isNotZero(updateNum)) {
				twit.removeImage();
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	@Override
	public Image findImage(Long twitId) {
		Image image = null;	
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_IMAGE);
			prepStatement.setLong(1, twitId);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				InputStream inputStream = resultSet.getBinaryStream(FIELD_IMAGE);
				BufferedImage bufImage = null;
				try {
					bufImage = ImageIO.read(inputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				image = new Image();
				image.setBufImage(bufImage);
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
		return image;
	}
}
