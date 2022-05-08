package com.revature.petapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.User;
import com.revature.petapp.services.UserService;

@RestController
@RequestMapping(path="/users")
@CrossOrigin(origins="http://localhost:4200")
public class UsersController {
	private UserService userServ;
	
	@Autowired
	public UsersController(UserService userServ) {
		this.userServ = userServ;
	}
	
	@PostMapping
	public ResponseEntity<User> register(@RequestBody User user) {
		try {
			user = userServ.register(user);
			return ResponseEntity.ok(user);
		} catch (UsernameAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") int userId) {
		User user = userServ.getUserById(userId);
		if (user != null) return ResponseEntity.ok(user);
		else return ResponseEntity.notFound().build();
	}

}
