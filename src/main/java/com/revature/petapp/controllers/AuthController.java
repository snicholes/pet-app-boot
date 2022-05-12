package com.revature.petapp.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.petapp.exceptions.IncorrectCredentialsException;
import com.revature.petapp.models.User;
import com.revature.petapp.services.UserService;

@RestController
@RequestMapping(path="/auth")
@CrossOrigin(origins="*")
public class AuthController {
	private UserService userServ;
	
	public AuthController(UserService userServ) {
		this.userServ=userServ;
	}
	
	@PostMapping
	public ResponseEntity<User> logIn(@RequestBody Map<String,String> credentials) {
		String username = credentials.get("username");
		String password = credentials.get("password");
		
		try {
			User user = userServ.logIn(username, password);
			return ResponseEntity.ok(user);
		} catch (IncorrectCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
