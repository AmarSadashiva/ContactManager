package com.example.ContactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.ContactManager.entities.User;
import com.example.ContactManager.repo.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService{
	 @Autowired
	 private UserRepo userRepository;
	     
	    @Override
	    public CustomUserDetails loadUserByUsername(String username)
	            throws UsernameNotFoundException {
	        User user = userRepository.getUserByUserName(username);
	         
	        if (user == null) {
	            throw new UsernameNotFoundException("Could not find user");
	        }
	         
	        return new CustomUserDetails(user);
	    }
}
