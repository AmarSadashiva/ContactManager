package com.example.ContactManager.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.ContactManager.entities.Contact;
import com.example.ContactManager.entities.User;
import com.example.ContactManager.repo.ContactRepo;
import com.example.ContactManager.repo.UserRepo;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	ContactRepo contactRepo;
	
	@RequestMapping("/showcontacts")
	public String showContacts(Model model, Principal p) {
		String name = p.getName();
		User user = this.userRepo.getUserByUserName(name);
		List<Contact> listContacts = contactRepo.findContactsByUser(user.getId());
		model.addAttribute("listContact", listContacts);
		return "showcontacts";
	}
	
	@RequestMapping("/addContact")
	public String addContact(Model model) {
		model.addAttribute("contact", new Contact());
		return "addcontact";
	}
	
	@RequestMapping("/save")
	public String saveContact(@ModelAttribute("contact") Contact contact, Principal P, @RequestParam("profileImage") MultipartFile file, Principal principal) throws IOException {
		String name = principal.getName();
		User user = this.userRepo.getUserByUserName(name);
		if (file.isEmpty()) {
			// if the file is empty then try our message
			System.out.println("File is empty");
			contact.setImage("contact.png");

		} else {
			// file the file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename());

			File saveFile = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			System.out.println("Image is uploaded");

		}
		user.getContacts().add(contact);

		contact.setUser(user);
		contactRepo.save(contact);
		
		
		return "redirect:/user/showcontacts";
	}
	
	@RequestMapping("/edit/{cid}")
	public ModelAndView	updateContact(@PathVariable(name="cid") Integer cid) {
		ModelAndView mav = new ModelAndView("updatecontact");
		Contact contact = contactRepo.findById(cid).get();
		mav.addObject("contact", contact);
		return mav;
	}
	
	@RequestMapping("delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid) {
		contactRepo.deleteById(cid);
		return "redirect:/user/showcontacts";
	}
	
	@RequestMapping("/showusers")
	public String displayUsers(Model model) {
		List<User> users = userRepo.findAll();
		model.addAttribute("users", users);
		return "showusers";
	}
	
	@RequestMapping("/profile")
	public String displayProfile() {
		return "showprofile";
	}
	
	@ModelAttribute
	public void addCommonData(Model model, Principal p) {
		String name = p.getName();
		User user = userRepo.getUserByUserName(name);
		Integer contactCount = contactRepo.getCountByUserName(name);
		model.addAttribute("user", user);
		model.addAttribute("contactCount", contactCount);
	}

}
