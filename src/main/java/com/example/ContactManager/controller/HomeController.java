package com.example.ContactManager.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.ContactManager.entities.User;
import com.example.ContactManager.repo.UserRepo;

@Controller
public class HomeController {
	
	@Autowired
	UserRepo userRepo;

	@RequestMapping("/")
	public String homePage() {
		return "welcome";
	}
	
	@RequestMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping("/save")
	public String saveUser(@ModelAttribute("user") User user) {
		user.setRole("ROLE_USER");
		user.setImageUrl("default.png");
		user.setEnabled(true);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	     
	    userRepo.save(user);
		return "redirect:/";
	}
	
	@RequestMapping("/signin")
	public String signinPage(Model model) {
		return "signin";
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView updateUser(@PathVariable("id") Integer id) {
		User user = userRepo.findById(id).get();
		ModelAndView mav = new ModelAndView("updateuser");
		mav.addObject("user", user);
		return mav;
	}
}
