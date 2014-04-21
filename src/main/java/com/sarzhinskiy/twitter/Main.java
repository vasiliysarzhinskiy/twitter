package com.sarzhinskiy.twitter;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sarzhinskiy.twitter.bean.city.City;
import com.sarzhinskiy.twitter.bean.country.Country;
import com.sarzhinskiy.twitter.bean.news.News;
import com.sarzhinskiy.twitter.bean.user.Gender;
import com.sarzhinskiy.twitter.bean.user.User;
import com.sarzhinskiy.twitter.bean.user.UserAdditionalInfo;
import com.sarzhinskiy.twitter.bean.user.UserRole;
import com.sarzhinskiy.twitter.dao.CityDAO;
import com.sarzhinskiy.twitter.dao.CountryDAO;
import com.sarzhinskiy.twitter.dao.DAOFactory;
import com.sarzhinskiy.twitter.dao.NewsDAO;
import com.sarzhinskiy.twitter.dao.TwitDAO;
import com.sarzhinskiy.twitter.dao.UserDAO;
import com.sarzhinskiy.twitter.dao.postgresql.city.CityDAOPostgreSQL;
import com.sarzhinskiy.twitter.dao.postgresql.country.CountryDAOPostgreSQL;
import com.sarzhinskiy.twitter.dao.postgresql.news.NewsDAOPostgreSQL;
import com.sarzhinskiy.twitter.dao.postgresql.user.UserDAOPostgreSQL;
import com.sarzhinskiy.twitter.jbdc.Connectable;
import com.sarzhinskiy.twitter.jbdc.ConnectionPool;

public class Main {
	
	public static void main(String[] args) throws TimeoutException {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {"SpringXMLConfig.xml"});
		ConnectionPool connPool = appContext.getBean("twitterPool", ConnectionPool.class);
		
		UserDAO userDAO = appContext.getBean("userDAO", UserDAOPostgreSQL.class);
		CountryDAO countryDAO = appContext.getBean("countryDAO", CountryDAOPostgreSQL.class);
		CityDAO cityDAO = appContext.getBean("cityDAO", CityDAOPostgreSQL.class);
		userDAO.removeAll();
		cityDAO.removeAll();
		countryDAO.removeAll();
		
		
		countryDAO.create(new Country("Ukraine"));
		cityDAO.create(new City("Kiev", "Ukraine"));
		countryDAO.create(new Country("South Korea"));
		
		User user = new User("mail5.ru", "sdds");
		user.setRole(UserRole.DEFAULT);
		userDAO.create(user);
		
		user = new User("msdds.ru", "sdsdds");
		userDAO.create(user);
		
		UserAdditionalInfo userInfo = new UserAdditionalInfo(user.getId());
		userInfo.setAboutYourself("lala la");
		userInfo.setCountry("Ukraine");
		userDAO.updateAdditionalInfo(userInfo);
		
		List<User> users = userDAO.findAll();
		System.out.println(users.size());

		userDAO.findByEmail("mail5.ru");
		userDAO.findByFullName("fdsssss");
		userDAO.findByAllFields("dsf", null, "", "", 1);
		userDAO.findById(user.getId());
		
		user.setSurname("dsds");
		userDAO.update(user);
		
		userDAO.findUserAdditionalInfoById(user.getId());
		
		User user1 = new User("A", "b");
		user1.setName("as");
		user1.setSurname("as");
		userDAO.create(user1);
		UserAdditionalInfo info1 = new UserAdditionalInfo(user1.getId());
		info1.setCountry("Ukraine");
		info1.setCity("Kiev");
		info1.setStatus(1);
		userDAO.updateAdditionalInfo(info1);
		
		userDAO.removeUser(user1.getId());
		
		User user2 = new User("em.com", "em");
		user2.setName("as");
		user2.setSurname("surrn");
		userDAO.create(user2);
		UserAdditionalInfo info2 = new UserAdditionalInfo(user2.getId());
		info2.setCountry("South Korea");
		info2.setStatus(1);
		userDAO.updateAdditionalInfo(info2);
		
		
		users = userDAO.findByAllFields(null, Gender.MALE, "Ukraine", "Kiev", 1);
		System.out.println(users);
		
		NewsDAO newsDAO = appContext.getBean("newsDAO", NewsDAOPostgreSQL.class);
		List<News> newsList = newsDAO.findAll();
		for (News news: newsList) {
			System.out.println(news);
		}
		
		City city = new City("Kiev", "Ukraine");
		cityDAO.create(city);
		
		city = new City("Zaporizhzhya", "Ukraine");
		cityDAO.create(city);
		
		city = new City("New York", "USA");
		cityDAO.create(city);
		
		System.out.println("\nAll cities:");
		for (City c: cityDAO.findAll()) {
			System.out.println(c);
		}

	}
	
	
}
