package com.sarzhinskiy.twitter.dao.postgresql.news;

public class NewsDAOPostgreSQLConstants {

	static final String FIELD_ID = "id";
	static final String FIELD_TEXT = "text";
	static final String FIELD_TIMESTAMP = "timestamp";
	static final String FIELD_LIKE_NUMBER = "like_number";
	static final String FIELD_STATUS = "status";
	static final String FIELD_IMAGE_ID = "id";
	static final String FIELD_IMAGE_NAME = "name";
	static final String FIELD_IMAGE_NEWS_ID = "news_id";
	static final String FIELD_IMAGE_COMMENT = "comment";
	static final String FIELD_IMAGE_FILE = "image";
	
	static final String QUERY_CREATE_NEWS = "INSERT INTO news (text, timestamp, like_number, status) VALUES(?, ?, ?, ?)";
	static final String QUERY_SELECT_NEWS_BY_ID = "SELECT * FROM news WHERE id = ?";
	static final String QUERY_SELECT_ALL = "SELECT * FROM news ORDER BY timestamp DESC";
	static final String QUERY_SELECT_LAST_NEWS = "SELECT * FROM news ORDER BY timestamp DESC LIMIT ?";
	static final String QUERY_SELECT_POPULAR_NEWS = "SELECT * FROM news ORDER BY like_number DESC, timestamp DESC LIMIT ?";
	static final String QUERY_UPDATE_NEWS = "UPDATE news SET text=? , timestamp=? , like_number=? , status=? WHERE id=?";
	static final String QUERY_UPDATE_LIKE_NUMBER_NEWS = "UPDATE news SET like_number=? WHERE id=?";
	static final String QUERY_DELETE_BY_ID = "DELETE FROM news WHERE id=?";
	static final String QUERY_DELETE_ALL_NEWS = "DELETE FROM news";
	
	static final String QUERY_INSERT_IMAGE = "INSERT INTO news_image (name, news_id, image, comment) VALUES (?, ?, ?, ?)";
	static final String QUERY_UPDATE_IMAGE = "UPDATE news_image SET name=? , news_id=? , image=? , comment=? WHERE id=?";
	static final String QUERY_SELECT_IMAGES_BY_NEWS = "SELECT * FROM news_image WHERE news_id=?";
	static final String QUERY_SELECT_IMAGES_BY_ID = "SELECT * FROM news_image WHERE id=?";
	static final String QUERY_DELETE_IMAGE = "DELETE FROM news_image WHERE id=?";
	static final String QUERY_DELETE_ALL_IMAGES = "DELETE FROM news_image";
	
}
