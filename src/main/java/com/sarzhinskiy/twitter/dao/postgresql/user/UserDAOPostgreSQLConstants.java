package com.sarzhinskiy.twitter.dao.postgresql.user;

public class UserDAOPostgreSQLConstants {
	static final String FIELD_ID = "id";
	static final String FIELD_USER_ID = "user_id";
	static final String FIELD_EMAIL = "email";
	static final String FIELD_PASSWORD_CIPHER = "password_cipher";
	static final String FIELD_SURNAME = "surname";
	static final String FIELD_NAME = "name";
	static final String FIELD_ROLE = "role";
	static final String FIELD_IS_BLOCKED = "is_blocked";
	
	static final String FIELD_GENDER = "gender";
	static final String FIELD_BIRTHDAY = "birthday";
	static final String FIELD_COUNTRY_ID = "county_id";
	static final String FIELD_CITY_ID = "city_id";
	static final String FIELD_ADDRESS = "address";
	static final String FIELD_PHONE = "phone";
	
	static final String FIELD_STATUS = "status";
	static final String FIELD_ABOUT_YOURSELF = "about_yourself";
	
	static final String FIELD_OBSERVER_ID = "id";
	static final String FIELD_USER_OBSERVER_ID = "user_observer_id";
	static final String FIELD_USER_OBSERVED_ID = "user_observed_id";
	
	static final String FIELD_IMAGE_ID = "id";
	static final String FIELD_IMAGE_NAME = "name";
	static final String FIELD_IMAGE_USER_ID = "user_id";
	static final String FIELD_IMAGE_COMMENT = "comment";
	static final String FIELD_IMAGE_FILE = "image";
	static final String FIELD_IMAGE_STATUS = "status";
	static final String FIELD_IMAGE_ALBUM = "album_name";
	
	static final  String  QUERY_CREATE_USER = "INSERT INTO \"user\" (email, password_cipher, surname, name, " +
			"role_id, is_blocked) VALUES(?, ?, ?, ?, (SELECT id FROM role WHERE name=?), ?)";
	static final String QUERY_CREATE_ADDITIONAL_INFO = "INSERT INTO user_additional_info "
			+ "(gender, birthday, country_id, city_id, address, phone, status, about_yourself, user_id) "
			+ "VALUES(?, ?, (SELECT id FROM country WHERE name=?), (SELECT id FROM city WHERE name=?), ?, ?, ?, ?, ?)";
	static final String QUERY_UPDATE_ADDITIONAL_INFO = "UPDATE user_additional_info SET "
			+ "gender=?, birthday=?, country_id=(SELECT id FROM country WHERE name=?), city_id=(SELECT id FROM city WHERE name=?), "
			+ "address=?, phone=?, status=?, about_yourself=? WHERE user_id=?";
	static final String QUERY_IS_EXIST_ID_IN_ADDITIONAL_INFO = "select * from user_additional_info where user_id=?";
	static final String QUERY_SELECT_ALL_USERS = "SELECT \"user\".*, role.name as role_name from \"user\" INNER JOIN role ON \"user\".role_id = role.id";
	static final String QUERY_SELECT_USER_BY_EMAIL = QUERY_SELECT_ALL_USERS + " WHERE \"user\".email=?";
	static final String QUERY_SELECT_USER_BY_ID = QUERY_SELECT_ALL_USERS + " WHERE \"user\".id=?";
	static final String QUERY_SELECT_USER_INFO_BY_ID = "SELECT user_additional_info.*, country.name as countryName, "
			+ "city.name as cityName FROM user_additional_info LEFT JOIN country ON user_additional_info.country_id=country.id  "
			+ "LEFT JOIN city ON user_additional_info.city_id=city.id WHERE user_id=?";
	
	static final String QUERY_SELECT_EMAILS_BY_FULL_NAME = "SELECT email FROM \"user\" WHERE surname=? AND name=?";
	static final String QUERY_SELECT_EMAILS_BY_SURNAME = "SELECT email FROM \"user\" WHERE surname=?";
	static final String QUERY_SELECT_EMAILS_BY_NAME = "SELECT email FROM \"user\" WHERE name=?";
	static final String QUERY_SELECT_PASSWORD_CIPHER_BY_EMAIL = "SELECT password_cipher FROM \"user\" WHERE email=?";
	static final String QUERY_UPDATE_USER = "UPDATE \"user\" SET email =?, password_cipher=?," +
			"surname=?, name=?, role_id=(SELECT id FROM role WHERE name=?), is_blocked=? WHERE id=?";
	
	static final String QUERY_CREATE_OBSERVED_USER = "INSERT INTO user_observer (user_observer_id, " +
			"user_observed_id) VALUES(?, ?)";
	static final String QUERY_SELECT_ALL_OBSERVED_USERS = "SELECT * FROM user_observer WHERE user_observer_id=?";
	static final String QUERY_DELETE_OBSERVED_USER = "DELETE FROM user_observer WHERE user_observer_id=? AND user_observed_id=?";
	static final String QUERY_DELETE_ALL_USERS = "DELETE FROM user_additional_info; DELETE FROM \"user\"";
	static final String QUERY_DELETE_USER_BY_ID = "DELETE FROM user_additional_info WHERE user_id=?; DELETE FROM \"user\" WHERE id=?";
	static final String QUERY_DELETE_ALL_OBSERVED_USERS = "DELETE FROM user_observer";
	
	static final String QUERY_INSERT_IMAGE = "INSERT INTO user_image (name, user_id, image, comment, status, album_name) VALUES (?, ?, ?, ?, ?, ?)";
	static final String QUERY_UPDATE_IMAGE = "UPDATE user_image SET name=?, user_id=?, image=?, comment=?, status=?, album_name=? WHERE id=?";
	static final String QUERY_SELECT_IMAGES_BY_USER = "SELECT * FROM user_image WHERE user_id=?";
	static final String QUERY_SELECT_IMAGES_BY_ID = "SELECT * FROM user_image WHERE id=?";
	static final String QUERY_DELETE_IMAGE = "DELETE FROM user_image WHERE id=?";
	static final String QUERY_DELETE_ALL_IMAGES = "DELETE FROM user_image";

}
