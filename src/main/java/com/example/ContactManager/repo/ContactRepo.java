package com.example.ContactManager.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ContactManager.entities.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer>{

	@Query("from Contact as c where c.user.id = :userId")
	public List<Contact> findContactsByUser(@Param("userId") int userId);
	
	@Query("select COUNT(c.id) from Contact c where c.user.email = :name")
	public Integer getCountByUserName(@Param("name") String name);
}