package com.sticky.notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sticky.notes.model.User;
import com.sticky.notes.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	public void saveUser(User user) {
		userRepo.save(user);
	}

	public boolean isValidUser(String email, String password) {
		User user = userRepo.findByEmail(email);
		return user!=null && user.getPassword().equals(password);
	}
	
	public Long getUserId(String email) {
		User user = userRepo.findByEmail(email);
		return user.getId();
	}
	
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

}
