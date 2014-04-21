package com.sarzhinskiy.twitter.bean.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;


public class User implements Serializable {
	private static final long serialVersionUID = -8573978067890864903L;
	
	private Long id;
	private String email;
	private String passwordCipher;
	private String surname;
	private String name;
	private UserRole role;
	private Boolean blocked = false;
	
	public User() {
	}
	
	public User(String email, String passwordCipher) {
		this();
		this.email = email;
		this.passwordCipher = passwordCipher;
	}
	
	public User(Long id, String email, String passwordCipher, String surname, String name) {
		this(email, passwordCipher);
		this.id = id;
		this.surname = surname;
		this.name = name;
	}
	
	@Override
	public String toString() {
		String userInfo = "User(" + surname + " " + name + ", email: " + email + ")";
		return userInfo;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof User)) {
			return false;
		}
		if (id == null) {
			return false;
		}
		return id.equals(((User) object).getId());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordCipher() {
		return passwordCipher;
	}

	public void setPasswordCipher(String passwordCipher) {
		this.passwordCipher = passwordCipher;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
	
}
