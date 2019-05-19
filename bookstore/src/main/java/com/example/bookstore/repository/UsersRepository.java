package com.example.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookstore.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	@Query("SELECT u FROM Users u where u.username = :username and u.password = :password")
	Users findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
