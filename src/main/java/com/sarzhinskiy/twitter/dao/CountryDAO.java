package com.sarzhinskiy.twitter.dao;

import java.util.List;

import com.sarzhinskiy.twitter.bean.country.Country;


public interface CountryDAO {
	public boolean create(Country country);
	public Country findById(Integer id);
	public Country findByName(String name);
	public List<Country> findAll();
	public boolean update(Country country);
	public boolean remove(Integer id);
	public boolean remove(String name);
	public boolean removeAll();

}
