package com.example.bookstore.model;

import java.math.BigDecimal;

public class Books {

	private String author_name;
	private BigDecimal price;
	private String book_name;
	private long id;
	private boolean is_recommended;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public boolean isIs_recommended() {
		return is_recommended;
	}

	public void setIs_recommended(boolean is_recommended) {
		this.is_recommended = is_recommended;
	}

}
