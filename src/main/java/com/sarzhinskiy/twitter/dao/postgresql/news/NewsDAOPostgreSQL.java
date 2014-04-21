package com.sarzhinskiy.twitter.dao.postgresql.news;

import static com.sarzhinskiy.twitter.dao.postgresql.UtilDAOPostgreSQL.close;
import static com.sarzhinskiy.twitter.dao.postgresql.UtilDAOPostgreSQL.getTimestampFrom;
import static com.sarzhinskiy.twitter.dao.postgresql.UtilDAOPostgreSQL.isNotZero;
import static com.sarzhinskiy.twitter.dao.postgresql.news.NewsDAOPostgreSQLConstants.*;

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

import com.sarzhinskiy.twitter.bean.news.News;
import com.sarzhinskiy.twitter.bean.news.NewsImage;
import com.sarzhinskiy.twitter.dao.NewsDAO;
import com.sarzhinskiy.twitter.jbdc.Connectable;
import com.sarzhinskiy.twitter.jbdc.ConnectionPool;


public class NewsDAOPostgreSQL implements NewsDAO {
	
	private ConnectionPool pool;
	

	public NewsDAOPostgreSQL(ConnectionPool pool) {
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
	public boolean create(News news) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_CREATE_NEWS, Statement.RETURN_GENERATED_KEYS);
			prepStatement.setString(1, news.getText());
			prepStatement.setTimestamp(2, getTimestampFrom(news.getDateTime()));
			prepStatement.setInt(3, news.getLikeNumber());
			prepStatement.setInt(4, news.getStatus());
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Long generatedID = resultSet.getLong(FIELD_ID);
				news.setId(generatedID);
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

	private News map(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong(FIELD_ID);
		String text = resultSet.getString(FIELD_TEXT);
		Timestamp timestamp = resultSet.getTimestamp(FIELD_TIMESTAMP);
		DateTime dateTime = new DateTime(timestamp);
		Integer likeNumber = resultSet.getInt(FIELD_LIKE_NUMBER);
		Integer status = resultSet.getInt(FIELD_STATUS);
		
		News news = new News(id, text, dateTime);
		news.setLikeNumber(likeNumber);
		news.setStatus(status);
		return news;		
	}
	
	@Override
	public News findById(Long id) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		News news = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_NEWS_BY_ID);
			prepStatement.setLong(1, id);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				news = map(resultSet);
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
		return news;
	}
	
	@Override
	public List<News> findAll() {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<News> newsList = new ArrayList<News>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_ALL);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				News news = map(resultSet);
				newsList.add(news);
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
		return newsList;
	}

	@Override
	public List<News> findLast(int numberNews) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<News> newsList = new ArrayList<News>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_LAST_NEWS);
			prepStatement.setInt(1, numberNews);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				News news = map(resultSet);
				newsList.add(news);
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
		return newsList;
	}

	@Override
	public boolean update(News news) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_NEWS);
			prepStatement.setString(1, news.getText());
			prepStatement.setTimestamp(2, getTimestampFrom(news.getDateTime()));
			prepStatement.setInt(3, news.getLikeNumber());
			prepStatement.setInt(4, news.getStatus());
			prepStatement.setLong(5, news.getId());
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
	public boolean updateLikeNumber(News news) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_LIKE_NUMBER_NEWS);
			prepStatement.setInt(1, news.getLikeNumber());
			prepStatement.setLong(2, news.getId());
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
	public List<News> findMostPopular(int numberNews) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<News> newsList = new ArrayList<News>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_POPULAR_NEWS);
			prepStatement.setInt(1, numberNews);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				News news = map(resultSet);
				newsList.add(news);
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
		return newsList;
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
			prepStatement = connection.prepareStatement(QUERY_DELETE_ALL_NEWS);
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
	public boolean insertImage(News news, NewsImage image) {
		FileInputStream fis = null;		
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_INSERT_IMAGE, Statement.RETURN_GENERATED_KEYS);
			prepStatement.setString(1, image.getName());
			long newsId = news.getId();
			prepStatement.setLong(2, newsId); //?
			image.setNewsId(newsId);
			File file = image.getImageFile();
			fis = new FileInputStream(file);
			prepStatement.setBinaryStream(3, fis, file.length());
			prepStatement.setString(4, image.getComment());
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Long generatedID = resultSet.getLong(FIELD_IMAGE_ID);
				image.setId(generatedID);
				news.addImage(image);
			}
		}
		catch (SQLException | FileNotFoundException exc) {
			exc.printStackTrace();
		}
		finally {
			close(fis);
			close(resultSet);
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	@Override
	public boolean removeAllNewsImages() {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_ALL_IMAGES);
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
	public boolean removeImage(News news, Long imageId) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_IMAGE);
			prepStatement.setLong(1, imageId);
			updateNum = prepStatement.executeUpdate();
			if (isNotZero(updateNum)) {
				news.removeImage(imageId);
			}
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
	
	private NewsImage mapImage(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong(FIELD_IMAGE_ID);
		String name = resultSet.getString(FIELD_IMAGE_NAME);
		Long newsId = resultSet.getLong(FIELD_IMAGE_NEWS_ID);
		String comment = resultSet.getString(FIELD_IMAGE_COMMENT);
		InputStream inputStream = resultSet.getBinaryStream(FIELD_IMAGE_FILE);
		BufferedImage bufImage = null;
		try {
			bufImage = ImageIO.read(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		NewsImage image = new NewsImage(id, name, newsId);
		image.setComment(comment);
		image.setBufImage(bufImage);		
		return image;
	}

	@Override
	public NewsImage findImage(Long id) {
		NewsImage image = null;	
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_IMAGES_BY_ID);
			prepStatement.setLong(1, id);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				image = mapImage(resultSet);
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

	@Override
	public List<NewsImage> findImagesByNews(Long newsId) {
		List <NewsImage> images = new ArrayList<NewsImage>();	
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_IMAGES_BY_NEWS);
			prepStatement.setLong(1, newsId);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				images.add(mapImage(resultSet));
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
		return images;

	}

	@Override
	public boolean updateImage(NewsImage image) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		InputStream fis = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_IMAGE);
			prepStatement.setString(1, image.getName());
			prepStatement.setLong(2, image.getNewsId());
			File file = image.getImageFile();
			fis = new FileInputStream(file);
			prepStatement.setBinaryStream(3, fis, file.length());
			prepStatement.setString(4, image.getComment());
			prepStatement.setLong(5, image.getId());
			updateNum = prepStatement.executeUpdate();
		}
		catch (SQLException | IOException exc) {
			exc.printStackTrace();
		}
		finally {
			close(fis);
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}
}
