package com.sarzhinskiy.twitter.dao;

import java.util.List;

import com.sarzhinskiy.twitter.bean.user.Gender;
import com.sarzhinskiy.twitter.bean.user.User;
import com.sarzhinskiy.twitter.bean.user.UserAdditionalInfo;
import com.sarzhinskiy.twitter.bean.user.UserImage;


public interface UserDAO {
	public boolean create(User user);
	public boolean updateAdditionalInfo(UserAdditionalInfo userInfo);
	public List<User> findAll();
	public User findById(Long id);
	public User findByEmail(String email);
	public UserAdditionalInfo findUserAdditionalInfoById(Long userId);
	public List<User> findByFullName(String fullName); //name or surname could be empty in search 
	public List<User> findByAllFields(String fullName, Gender gender, String country, String city, Integer status);//full search: variable could be empty in search
	public String findPasswordCipherByEmail(String email);
	public boolean update(User user);
	public boolean addObservedUser(User observerUser, User observedUser); 
	public boolean removeObservedUser(User observerUser, User observedUser); //remove for user  viewed user
	public List<User> findAllObservedUser(Long observerId);
	public boolean removeUser(Long userId);
	public boolean removeAll();
	public boolean removeAllObservedUsers();
	public boolean insertImage(User user, UserImage image);
	public boolean updateImage(UserImage image);
	public boolean removeAllImages();
	public boolean removeImage(User user, Long imageId);
	public UserImage findImage(Long imageId);
	public List<UserImage> findImagesByUser(Long userId);

}
