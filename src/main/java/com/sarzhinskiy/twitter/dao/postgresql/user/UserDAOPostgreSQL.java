package com.sarzhinskiy.twitter.dao.postgresql.user;

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
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

import org.joda.time.LocalDate;

import com.sarzhinskiy.twitter.bean.user.Gender;
import com.sarzhinskiy.twitter.bean.user.User;
import com.sarzhinskiy.twitter.bean.user.UserAdditionalInfo;
import com.sarzhinskiy.twitter.bean.user.UserImage;
import com.sarzhinskiy.twitter.bean.user.UserRole;
import com.sarzhinskiy.twitter.dao.UserDAO;
import com.sarzhinskiy.twitter.jbdc.Connectable;
import com.sarzhinskiy.twitter.jbdc.ConnectionPool;

import static com.sarzhinskiy.twitter.dao.postgresql.UtilDAOPostgreSQL.*;
import static com.sarzhinskiy.twitter.dao.postgresql.user.UserDAOPostgreSQLConstants.*;

public class UserDAOPostgreSQL implements UserDAO {
	
	private ConnectionPool pool;
	
	public UserDAOPostgreSQL(ConnectionPool pool) {
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
	public boolean create(User user) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
			prepStatement.setString(1, user.getEmail());
			prepStatement.setString(2, user.getPasswordCipher());
			prepStatement.setString(3, user.getSurname());
			prepStatement.setString(4, user.getName());
			UserRole role = user.getRole();
			if (role == UserRole.ADMIN) {
				prepStatement.setString(5, "admin");
			}
			else {
				prepStatement.setString(5, "user");
				user.setRole(UserRole.USER);
			}
			prepStatement.setBoolean(6, user.isBlocked());			
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Long generatedId = resultSet.getLong(FIELD_ID);
				user.setId(generatedId);
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
	public boolean updateAdditionalInfo(UserAdditionalInfo userInfo) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_IS_EXIST_ID_IN_ADDITIONAL_INFO);
			prepStatement.setLong(1, userInfo.getId());
			resultSet = prepStatement.executeQuery();
			boolean isExistIdInTableAdditionalInfo = false;
			if (resultSet.next()) { 
				isExistIdInTableAdditionalInfo = true;
			}
			resultSet.close();
			prepStatement.close();
			if (isExistIdInTableAdditionalInfo) {
				prepStatement = connection.prepareStatement(QUERY_UPDATE_ADDITIONAL_INFO);
			}
			else {
				prepStatement = connection.prepareStatement(QUERY_CREATE_ADDITIONAL_INFO);
			}
			Boolean genderDB = getGender(userInfo.getGender());
			if (genderDB != null) {
				prepStatement.setBoolean(1, genderDB); 		
			}
			else {
				prepStatement.setNull(1, java.sql.Types.BOOLEAN);
			}			
			LocalDate birthdayDate = userInfo.getBirthday();
			if (birthdayDate != null) {
				prepStatement.setDate(2, getDateFrom(birthdayDate));
			}
			else {
				prepStatement.setNull(2, java.sql.Types.DATE);
			}
			prepStatement.setString(3, userInfo.getCountry());
			prepStatement.setString(4, userInfo.getCity());
			prepStatement.setString(5, userInfo.getAddress());
			prepStatement.setString(6, userInfo.getPhone());
			prepStatement.setInt(7, userInfo.getStatus());
			prepStatement.setString(8, userInfo.getAboutYourself());			
			prepStatement.setLong(9, userInfo.getId());
			updateNum = prepStatement.executeUpdate();
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

	private UserAdditionalInfo mapUserInfo(ResultSet resultSet) throws SQLException {
		long id = resultSet.getLong(FIELD_USER_ID);
		Gender gender = getGender(resultSet.getBoolean(FIELD_GENDER));
		String countryName = resultSet.getString("countryName");
		Date birthday = resultSet.getDate(FIELD_BIRTHDAY);
		LocalDate birthdayDate = new LocalDate(birthday);
		String cityName = resultSet.getString("cityName");
		String address = resultSet.getString(FIELD_ADDRESS);
		String phone = resultSet.getString(FIELD_PHONE);
		int status = resultSet.getInt(FIELD_STATUS);
		String aboutYourself = resultSet.getString(FIELD_ABOUT_YOURSELF);
		
		UserAdditionalInfo userInfo = new UserAdditionalInfo(id);
		userInfo.setGender(gender);
		userInfo.setCountry(countryName);
		userInfo.setBirthday(birthdayDate);
		userInfo.setCity(cityName);
		userInfo.setAddress(address);
		userInfo.setPhone(phone);
		userInfo.setStatus(status);
		userInfo.setAboutYourself(aboutYourself);
		return userInfo;
	}

	private User mapUser(ResultSet resultSet) throws SQLException {		
		long id = resultSet.getLong(FIELD_ID);
		String email = resultSet.getString(FIELD_EMAIL);
		String passwordCipher = resultSet.getString(FIELD_PASSWORD_CIPHER);
		String surname= resultSet.getString(FIELD_SURNAME);
		String name = resultSet.getString(FIELD_NAME);
		String roleName = resultSet.getString("role_name");
		boolean isBlocked = resultSet.getBoolean(FIELD_IS_BLOCKED);

		User user = new User(id, email, passwordCipher, surname, name);
		user.setRole(getRole(roleName));
		user.setBlocked(isBlocked);
		return user;
	}
	
	private UserRole getRole(String roleName) {
		switch (roleName) {
		case "admin":
			return UserRole.ADMIN;
		case "user": 
			return UserRole.USER;
		}
		return UserRole.DEFAULT;
	}
	
	private Gender getGender(Boolean genderDB) {
		if (genderDB == null) {
			return null;
		}
		if (!genderDB) {
			return Gender.MALE;
		}
		return Gender.FEMALE;
	}
	
	private Boolean getGender(Gender gender) {
		if (gender == null) {
			return null;
		}
		if (gender.equals(Gender.MALE)) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<User> findAll() {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<User> users = new ArrayList<User>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_ALL_USERS);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				User user = mapUser(resultSet);
				users.add(user);
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
		return users;
	}

	@Override
	public User findByEmail(String email) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_USER_BY_EMAIL);
			prepStatement.setString(1, email);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				user = mapUser(resultSet);
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
		return user;
	}

	@Override
	public User findById(Long id) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_USER_BY_ID);
			prepStatement.setLong(1, id);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				user = mapUser(resultSet);
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
		return user;
	}
	@Override
	public UserAdditionalInfo findUserAdditionalInfoById(Long userId) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		UserAdditionalInfo userInfo = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_USER_INFO_BY_ID);
			prepStatement.setLong(1, userId);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				userInfo = mapUserInfo(resultSet);
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
		return userInfo;
	}
	
	@Override
	public List<User> findByFullName(String fullName) { //surname and name
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<User> users = new ArrayList<User>();
		Set<String> usersEmail = new HashSet<String>();
		String [] nameInfo = fullName.trim().split("[ ]+");
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			if (nameInfo.length == 2) { 
				//first is surname, second is name
				prepStatement = connection.prepareStatement(QUERY_SELECT_EMAILS_BY_FULL_NAME);
				prepStatement.setString(1, nameInfo[0]);
				prepStatement.setString(2, nameInfo[1]);
				resultSet = prepStatement.executeQuery();
				while (resultSet.next()) {
					String email = resultSet.getString(FIELD_EMAIL);
					usersEmail.add(email);
				}
				close(resultSet);
				close(prepStatement);
				
				//first is name, second is surname
				prepStatement = connection.prepareStatement(QUERY_SELECT_EMAILS_BY_FULL_NAME);
				prepStatement.setString(1, nameInfo[1]);
				prepStatement.setString(2, nameInfo[0]);
				resultSet = prepStatement.executeQuery();
				while (resultSet.next()) {
					String email = resultSet.getString(FIELD_EMAIL);
					usersEmail.add(email);
				}
			}
			else if (nameInfo.length == 1) {
				//only surname
				prepStatement = connection.prepareStatement(QUERY_SELECT_EMAILS_BY_SURNAME);
				prepStatement.setString(1, nameInfo[0]);
				resultSet = prepStatement.executeQuery();
				while (resultSet.next()) {
					String email = resultSet.getString(FIELD_EMAIL);
					usersEmail.add(email);
				}
				close(resultSet);
				close(prepStatement);
				
				//only name
				prepStatement = connection.prepareStatement(QUERY_SELECT_EMAILS_BY_NAME);
				prepStatement.setString(1, nameInfo[0]);
				resultSet = prepStatement.executeQuery();
				while (resultSet.next()) {
					String email = resultSet.getString(FIELD_EMAIL);
					usersEmail.add(email);
				}
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
		for (String email: usersEmail) {
			User user = findByEmail(email);
			users.add(user);
		}
		return users;
	}

	@Override
	public String findPasswordCipherByEmail(String email) { //for verification
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		String passwordCipher = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_PASSWORD_CIPHER_BY_EMAIL);
			prepStatement.setString(1, email);
			resultSet = prepStatement.executeQuery();
			if (resultSet.next()) {
				passwordCipher = resultSet.getString(FIELD_PASSWORD_CIPHER);
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
		return passwordCipher;
	}
	
	@Override
	public List<User> findByAllFields(String fullName, Gender gender, String country, String city, Integer status) {
		List<User> users = new ArrayList<User>();
		if (fullName != null && !fullName.isEmpty()) {
			users = findByFullName(fullName);
			for (Iterator<User> iterator = users.iterator(); iterator.hasNext(); ) {
				User user = iterator.next();
				UserAdditionalInfo userInfo = findUserAdditionalInfoById(user.getId());
				if ((gender != null && !gender.equals(userInfo.getGender())) 
						|| (status != null && userInfo.getStatus() != status) 
						|| (country != null && !country.isEmpty() && !country.equals(userInfo.getCountry()))
						|| (city != null &&  !city.isEmpty() && !city.equals(userInfo.getCity()))) {
					iterator.remove();
				}
			}
		}
		else {
			Connectable connectable = null;
			Connection connection = null;
			PreparedStatement prepStatement = null;
			ResultSet resultSet = null;
			try {
				connectable = getConnectable();
				connection = getConnection(connectable);
				String query = "SELECT user_id FROM user_additional_info LEFT JOIN country ON user_additional_info.country_id=country.id  "
						+ "LEFT JOIN city ON user_additional_info.city_id=city.id";
				query = query + getRequiredQuery(gender, country, city, status);
				prepStatement = connection.prepareStatement(query);
				resultSet = prepStatement.executeQuery();
				List<Long> usersId = new ArrayList<Long>();
				while (resultSet.next()) {
					usersId.add(resultSet.getLong("user_id"));
				}
				for (Long id: usersId) {
					users.add(findById(id));
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
		}
		return users;
	}
	
	private String getRequiredQuery(Gender gender, String country, String city, Integer status) {
		String query = "";
		List<String> uppliedData = new ArrayList<String>();
		if (gender != null) {
			uppliedData.add(" user_additional_info.gender='" + getGender(gender) + "'");
		}
		if (status != null) {
			uppliedData.add(" user_additional_info.status='" + status + "'");
		}
		if (country != null && !country.isEmpty()) {
			uppliedData.add(" country.name='" + country + "'");
		}
		if (city != null &&  !city.isEmpty()) {
			uppliedData.add(" city.name='" + city + "'");
		}
		for (int i = 0; i < uppliedData.size(); i++) {
			if (i == 0) {
				query = query + " WHERE ";
			}
			query = query + uppliedData.get(i);
			if (i < uppliedData.size() - 1) {
				query = query +  " and ";
			}
		}
		return query;
	}

	@Override
	public boolean update(User user) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_USER);
			prepStatement.setString(1, user.getEmail());
			prepStatement.setString(2, user.getPasswordCipher());
			prepStatement.setString(3, user.getSurname());
			prepStatement.setString(4, user.getName());
			String role = user.getRole().toString().toLowerCase();
			prepStatement.setString(5, role);
			prepStatement.setBoolean(6, user.isBlocked());
			prepStatement.setLong(7, user.getId());
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
	public boolean addObservedUser(User observerUser, User observedUser) {		
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_CREATE_OBSERVED_USER, Statement.RETURN_GENERATED_KEYS);
			prepStatement.setLong(1, observerUser.getId());
			prepStatement.setLong(2, observedUser.getId());
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
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
	public List<User> findAllObservedUser(Long observerId) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		List<User> observedUsers = new ArrayList<User>();
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_ALL_OBSERVED_USERS);
			prepStatement.setLong(1, observerId);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				long observedId = resultSet.getLong(FIELD_USER_OBSERVED_ID);				
				User user = findById(observedId);
				observedUsers.add(user);
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
		return observedUsers;
	}
	
	@Override
	public boolean removeObservedUser(User observerUser, User observedUser) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_OBSERVED_USER);
			prepStatement.setLong(1, observerUser.getId());
			prepStatement.setLong(2, observedUser.getId());
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
	public boolean removeUser(Long userId){
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_USER_BY_ID);
			prepStatement.setLong(1, userId);
			prepStatement.setLong(2, userId);
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
	public boolean removeAll(){
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_ALL_USERS);
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
	public boolean removeAllObservedUsers() {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_ALL_OBSERVED_USERS);
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
	public boolean insertImage(User user, UserImage image) {
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
			if (image.getUserId() != null) {
				prepStatement.setLong(2, image.getUserId());
			}
			else {
				image.setUserId(user.getId());
				prepStatement.setLong(2, image.getUserId());
			}
			File file = image.getImageFile();
			fis = new FileInputStream(file);
			prepStatement.setBinaryStream(3, fis, file.length());
			prepStatement.setString(4, image.getComment());
			prepStatement.setInt(5, image.getStatus());
			prepStatement.setString(6, image.getAlbumName());
			updateNum = prepStatement.executeUpdate();
			resultSet = prepStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Long generatedID = resultSet.getLong(FIELD_IMAGE_ID);
				image.setImageId(generatedID);
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
	public boolean updateImage(UserImage image) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_UPDATE_IMAGE);
			prepStatement.setString(1, image.getName());
			prepStatement.setLong(2, image.getUserId());
			File file = image.getImageFile();
			InputStream fis = new FileInputStream(file);
			prepStatement.setBinaryStream(3, fis, file.length());
			prepStatement.setString(4, image.getComment());
			prepStatement.setInt(5, image.getStatus());
			prepStatement.setString(6, image.getAlbumName());
			prepStatement.setLong(7, image.getImageId());
			updateNum = prepStatement.executeUpdate();
		}
		catch (SQLException | IOException exc) {
			exc.printStackTrace();
		}
		finally {
			close(prepStatement);
			closeConnection(connectable);
		}
		return isNotZero(updateNum);
	}

	@Override
	public boolean removeAllImages() {
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
	public boolean removeImage(User user, Long imageId) {
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		UserImage image = findImage(imageId);
		if (image == null) {
			return false;
		}
		int updateNum = 0;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_DELETE_IMAGE);
			prepStatement.setLong(1, imageId);
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
	public UserImage findImage(Long imageId) {
		UserImage image = null;	
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_IMAGES_BY_ID);
			prepStatement.setLong(1, imageId);
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
	public List<UserImage> findImagesByUser(Long userId) {
		List <UserImage> images = new ArrayList<UserImage>();	
		Connectable connectable = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			connectable = getConnectable();
			connection = getConnection(connectable);
			prepStatement = connection.prepareStatement(QUERY_SELECT_IMAGES_BY_USER);
			prepStatement.setLong(1, userId);
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
	
	private UserImage mapImage(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong(FIELD_IMAGE_ID);
		String name = resultSet.getString(FIELD_IMAGE_NAME);
		Long userId = resultSet.getLong(FIELD_IMAGE_USER_ID);
		String comment = resultSet.getString(FIELD_IMAGE_COMMENT);
		InputStream inputStream = resultSet.getBinaryStream(FIELD_IMAGE_FILE);
		String albumName = resultSet.getString(FIELD_IMAGE_ALBUM);
		Integer status = resultSet.getInt(FIELD_IMAGE_STATUS);
		BufferedImage bufImage = null;
		try {
			bufImage = ImageIO.read(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		UserImage image = new UserImage(name, userId);
		image.setImageId(id);
		image.setComment(comment);
		image.setBufImage(bufImage);
		image.setStatus(status);
		image.setAlbumName(albumName);
		return image;
	}

}
