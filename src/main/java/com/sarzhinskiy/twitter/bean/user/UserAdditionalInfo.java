package com.sarzhinskiy.twitter.bean.user;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class UserAdditionalInfo implements Serializable {
	private static final long serialVersionUID = 7093190398419921139L;

	private Long id;
	private Gender gender; 
	private LocalDate birthday;
	private String country = "";
	private String city = "";
	private String address = "";
	private String phone = ""; 
	private Integer status = new Integer(0);
	private String aboutYourself = "";
	
	public UserAdditionalInfo() {
	}

	public UserAdditionalInfo(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAboutYourself() {
		return aboutYourself;
	}

	public void setAboutYourself(String aboutYourself) {
		this.aboutYourself = aboutYourself;
	}
	
}
