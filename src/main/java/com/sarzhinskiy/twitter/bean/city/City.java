package com.sarzhinskiy.twitter.bean.city;

import java.io.Serializable;


public class City implements Serializable {
	private static final long serialVersionUID = -150325013893375275L;
	
	private Integer id;
	private String name;
	private String country;
	private Integer status = new Integer(0);
	
	public City() {
	}
	
	public City(String name) {
		this();
		this.name = name;
	}
	
	public City(String name, String country) {
		this(name);
		this.country = country;
	}
	
	
	public City(Integer id, String name, String country) {
		this(name, country);
		this.id = id;
	}
	
	@Override
	public String toString() {
		String info = "City(" + name + ", country: " + country + ")";
		return info;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof City)) {
			return false;
		}
		if (id == null) {
			return false;
		}
		return (id.equals(((City) object).getId()));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	

}
