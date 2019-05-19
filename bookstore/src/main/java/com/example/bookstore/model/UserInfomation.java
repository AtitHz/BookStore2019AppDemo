package com.example.bookstore.model;

public class UserInfomation {

//	{
//	 Response:
//	 200 OK
//	  "name": "john",
//	 "surname": "doe",
//	 "date_of_birth": "15/01/1985",
//	 
//	    
//	 "books": [1, 4]
//	}

	private long id;
	private String name;
	private String surname;
	private String date_of_birth;
	private long[] books = {};

	public UserInfomation() {

	}

	public UserInfomation(String name, String surname, String date_of_birth) {
		this.name = name;
		this.surname = surname;
		this.date_of_birth = date_of_birth;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public long[] getBooks() {
		return books;
	}

	public void setBooks(long[] books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return "UserInfomation [id=" + id + ", name=" + name + ", surname=" + surname + ", date_of_birth="
				+ date_of_birth + "]";
	}

}
