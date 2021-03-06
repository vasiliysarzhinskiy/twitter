package com.sarzhinskiy.twitter.dao;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sarzhinskiy.twitter.bean.country.Country;
import com.sarzhinskiy.twitter.dao.CountryDAO;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/SpringXMLConfigTest.xml"})
public class CountryDAOTest {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private CountryDAO countryDAO;
	
	private static final String SElECT_COUNT_COUNTRIES = "select count(*) from country";
	private static final String DELETE_COUNTRIES = "delete from country";
	
	@Before
	public void clearDBTable() {
		jdbcTemplate.execute(DELETE_COUNTRIES);
	}
	
	@Test
	public void testCreateNoExceptions() {
		Country country = new Country("A");
		countryDAO.create(country);
	}
	
	@Test
	public void testCreate() {
		Country country = new Country("A");
		countryDAO.create(country);
		int size = jdbcTemplate.queryForObject(SElECT_COUNT_COUNTRIES, Integer.class);
		assertEquals(1, size);
	}
	
	@Test
	public void testCreateAutoGeneratedId() {
		Country country = new Country("A");
		countryDAO.create(country);
		assertNotNull(country.getId());
	}
	
	@Test
	public void testFindByName() {
		String countryName = "A";
		Country country = new Country(countryName);
		countryDAO.create(country);
		Country findedCountry = countryDAO.findByName(countryName);
		assertNotNull(findedCountry);
		assertEquals("A", findedCountry.getName());
	}
	
	@Test
	public void testFindById() {
		Country country = new Country("A");
		countryDAO.create(country);
		Country findedCountry = countryDAO.findById(country.getId());
		assertNotNull(findedCountry);
		assertEquals(country.getId(), findedCountry.getId());
	}
	
	@Test
	public void testCreateTwoCountries() {
		Country country = new Country("A");
		countryDAO.create(country);
		country = new Country("B");
		countryDAO.create(country);
		int size = jdbcTemplate.queryForObject(SElECT_COUNT_COUNTRIES, Integer.class);
		int expectedSize = 2;
		assertEquals(expectedSize, size);
	}
	
	@Test
	public void testFindAll() {
		Country country = new Country("A");
		countryDAO.create(country);
		country = new Country("B");
		countryDAO.create(country);
		country = new Country("C");
		countryDAO.create(country);
		List<Country> countries = countryDAO.findAll();
		int size = countries.size();
		assertEquals(3, size);
	}
	
	@Test
	public void testRemoveById() {
		Country country = new Country("A");
		countryDAO.create(country);
		countryDAO.remove(country.getId());
		int size = jdbcTemplate.queryForObject(SElECT_COUNT_COUNTRIES, Integer.class);
		assertEquals(0, size);
	}
	
	@Test
	public void testRemoveByName() {
		String countryName = "A";
		Country country = new Country(countryName);
		countryDAO.create(country);
		countryDAO.remove(countryName);
		int size = jdbcTemplate.queryForObject("select count(*) from country where name='A'", Integer.class);
		assertEquals(0, size);
	}
	
	@Test
	public void testRemoveAll() {
		Country country = new Country("A");
		countryDAO.create(country);
		country = new Country("B");
		countryDAO.create(country);
		countryDAO.removeAll();
		int size = jdbcTemplate.queryForObject(SElECT_COUNT_COUNTRIES, Integer.class);
		assertEquals(0, size);
	}
	
	@Test
	public void testUpdate() {
		Country country = new Country("A");
		countryDAO.create(country);
		String newCountryName = "AAA";
		country.setName(newCountryName);
		countryDAO.update(country);
		String updCountryName = jdbcTemplate.queryForObject("select name from country where id=" 
				+ country.getId(), String.class);
		assertEquals(newCountryName, updCountryName);
	}
	
	@Test
	public void testUpdateNotModifyId() {
		Country country =
				new Country("A");
		countryDAO.create(country);
		String newCountryName = "AAA";
		country.setName(newCountryName);
		countryDAO.update(country);
		Integer updCountryId = jdbcTemplate.queryForObject("select id from country where name='AAA'", 
				Integer.class);
		assertEquals(country.getId(), updCountryId);
	}
	
}
