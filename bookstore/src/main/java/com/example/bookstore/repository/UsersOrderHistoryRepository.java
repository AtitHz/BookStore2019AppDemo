package com.example.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookstore.model.UsersOrderHistory;

@Repository
public interface UsersOrderHistoryRepository extends JpaRepository<UsersOrderHistory, Long> {

	@Query("SELECT u FROM UsersOrderHistory u where u.ownerUserId = :ownerUserId")
	List<UsersOrderHistory> findByOwnerUserId(@Param("ownerUserId") long ownerUserId);
	
	@Query("DELETE FROM UsersOrderHistory u where u.ownerUserId = :ownerUserId")
	void deleteOrderHistory(@Param("ownerUserId") long ownerUserId);
}
