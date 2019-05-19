package com.example.bookstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class Users {

//	 Request:
//	 {"username": "john.doe", "password": "thisismysecret", "date_of_birth": "15/01/1985"}

	private long id;
	private String username;
	private String password;
	private String date_of_birth;

	public Users() {

	}

	public Users(String username, String password, String date_of_birth) {
		this.username = username;
		this.password = password;
		this.date_of_birth = date_of_birth;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "USERNAME", nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "DATE_OF_BIRTH", nullable = false)
	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", password=" + password + ", date_of_birth="
				+ date_of_birth + "]";
	}

}
