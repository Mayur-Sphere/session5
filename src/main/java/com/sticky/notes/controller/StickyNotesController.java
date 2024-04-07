package com.sticky.notes.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sticky.notes.model.StickyNotes;
import com.sticky.notes.model.User;
import com.sticky.notes.service.StickyNotesService;
import com.sticky.notes.service.UserService;

@Controller
public class StickyNotesController {
	
	@Autowired
	private StickyNotesService noteService;
	
	@Autowired
	private UserService userService;
	
	private String currentUser;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String signUp() {
		return "signup";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("username", null);
		return "login";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, Model model) {
		try {
			userService.saveUser(user);
			model.addAttribute("successMessage", "User details saved successfully.");
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error saving user details. Please try again.");
		}
		return "signup";
	}
	
	@PostMapping("/loginuser")
	public String loginUser(String email, String password, Model model) {
		if (userService.isValidUser(email, password)) {
			this.currentUser = email;
            return "/dashboard";
        } else {
        	this.currentUser = null;
        	model.addAttribute("error","Invalid email or password. Please try again.");
            return "login";
        }
	}
	
	@GetMapping("/addnotes")
	public String addNotes() {
		return "addnotes";
	}
	
	@PostMapping("/saveNotes")
	public String submitNotes(@ModelAttribute StickyNotes notes, Principal principal) {
		User user = userService.findByEmail(this.currentUser);
		notes.setUser(user);
		noteService.saveNotes(notes);
		return "addnotes";
	}
	
	@GetMapping("/viewNote")
	public String viewNotes(Model model) {
		User user = userService.findByEmail(this.currentUser);
		List<StickyNotes> notesList = noteService.findByUserId(user.getId());
		model.addAttribute("notes",notesList);
		System.out.println(notesList.toString());
		return "viewnote";
	}
	
	@PostMapping("/editnote/{id}")
	public String editNotes(@PathVariable("id") Long id, @ModelAttribute StickyNotes notes, RedirectAttributes redirectAttributes) {
		noteService.saveEditedNotes(notes,id);
		redirectAttributes.addFlashAttribute("editSuccess", true);
		return "viewnote";
	}
	
	@PostMapping("/deletenote/{id}")
	public String deleteNotes(@PathVariable("id") Long id, @ModelAttribute StickyNotes notes) {
		notes.setId(id);
		noteService.deleteNotes(notes);
		return "addnotes";
	}
	
}
