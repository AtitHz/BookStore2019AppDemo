package com.example.bookstore.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS_ORDER_HISTORY")
public class UsersOrderHistory {

	private long id;
	private String author_name;
	private BigDecimal price;
	private String book_name;
	private boolean is_recommended;
	private long ownerUserId;
	private long bookId;

	public UsersOrderHistory() {

	}

	public UsersOrderHistory(Long ownerUserId, String author_name, BigDecimal price, String book_name,
			boolean is_recommended) {
		this.ownerUserId = ownerUserId;
		this.author_name = author_name;
		this.price = price;
		this.book_name = book_name;
		this.is_recommended = is_recommended;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "OWNER_USER_ID", nullable = false)
	public long getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(long ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	@Column(name = "AUTHOR_NAME", nullable = false)
	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	@Column(name = "PRICE", nullable = false)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "BOOK_NAME", nullable = false)
	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	@Column(name = "IS_RECOMMENDED", nullable = false)
	public boolean isIs_recommended() {
		return is_recommended;
	}

	public void setIs_recommended(boolean is_recommended) {
		this.is_recommended = is_recommended;
	}

	@Column(name = "BOOK_ID", nullable = false)
	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

}
