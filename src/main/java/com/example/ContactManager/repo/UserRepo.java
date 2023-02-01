package com.example.ContactManager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ContactManager.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{

	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);

	

}
