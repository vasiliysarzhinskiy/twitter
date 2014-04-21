package com.sarzhinskiy.twitter.dao.postgresql.twit;

public class TwitDAOPostgreSQLConstants {

	static final String FIELD_ID = "id";
	static final String FIELD_OWNER_ID = "owner_id";
	static final String FIELD_TEXT = "text";
	static final String FIELD_TIMESTAMP = "timestamp";
	static final String FIELD_LIKE_NUMBER = "like_number";
	static final String FIELD_STATUS = "status";
	static final String FIELD_IMAGE = "image";
	
	static final String QUERY_CREATE_TWIT = "INSERT into twit (owner_id, text, timestamp, like_number, status) VALUES(?, ?, ?, ?, ?)";
	static final String QUERY_UPDATE_TWIT = "UPDATE twit SET owner_id=?, text=?, timestamp=?, like_number=?, status=? WHERE id=?";
	static final String QUERY_UPDATE_LIKE_NUMBER_TWIT = "UPDATE twit SET like_number=? WHERE id=?";
	static final String QUERY_SELECT_BY_ID = "SELECT * FROM twit WHERE id = ?";
	static final String QUERY_SELECT_BY_OWNER_EMAIL = "SELECT twit.* FROM twit, \"user\" WHERE \"user\".email=? AND \"user\".id=twit.owner_id ORDER BY timestamp DESC";
	static final String QUERY_SELECT_BY_OWNER_ID = "SELECT * FROM twit WHERE owner_id=? ORDER BY timestamp DESC";
	static final String QUERY_SELECT_LAST_TWITS_BY_OWNER_ID = "SELECT * FROM twit WHERE owner_id=? ORDER BY timestamp DESC LIMIT ?";  //or ROW_NUMBER () 
	static final String QUERY_DELETE_BY_ID = "DELETE FROM twit WHERE id=?";
	static final String QUERY_DELETE_ALL = "DELETE FROM twit";
	
	static final String QUERY_CREATE_IMAGE = "UPDATE twit SET image=? WHERE id=?";
	static final String QUERY_SELECT_IMAGE = "SELECT image FROM twit WHERE id=?";
	static final String QUERY_DELETE_IMAGE = "UPDATE twit SET image=null WHERE id=?";
}
