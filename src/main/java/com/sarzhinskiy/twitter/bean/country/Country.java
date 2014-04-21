package com.sarzhinskiy.twitter.bean.country;

import java.io.Serializable;


public class Country implements Serializable {
	private static final long serialVersionUID = 3351585464846936061L;
	
	private Integer id;
	private String name;
	
	public Country() {
	}
	
	public Country(String name) {
		this();
		this.name = name;
	}
	
	public Country(Integer id, String name) {
		this(name);
		this.id = id;
	}
	
	@Override
	public String toString() {
		String info = "Country(" + name + ")";
		return info;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Country)) {
			return false;
		}
		if (id == null) {
			return false;
		}
		return (id.equals(((Country) object).getId()));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name.toString();
	}

	public void setName(String name) {
		this.name = name;
	}

}
