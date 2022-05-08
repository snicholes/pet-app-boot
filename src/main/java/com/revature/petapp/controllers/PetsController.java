package com.revature.petapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.User;
import com.revature.petapp.services.UserService;

// RestController stereotype puts @ResponseBody on top of every method implicitly
// (this means that the methods return resources instead of views)
@RestController
@RequestMapping(path="/pets") // all endpoints in this class start with /pets
@CrossOrigin(origins="http://localhost:4200") // where we're accepting requests from
public class PetsController {
	private UserService userServ;
	
	@Autowired
	public PetsController(UserService userServ) {
		this.userServ = userServ;
	}

	// the method for handling each endpoint is more intuitive:
	// the return type is a response entity, and the parameters are
	// whatever you want from the request (path variables, 
	// request body, query params, etc)
	//@RequestMapping(path="/pets", method = RequestMethod.GET)
	@GetMapping
	//@ResponseBody // tells Spring to skip the ViewResolver and just return a resource (rather than a view)
	public ResponseEntity<List<Pet>> getPets() {
		List<Pet> pets = userServ.viewAvailablePets();
		return ResponseEntity.ok(pets);
	}
	
	@GetMapping(path="/{petId}")
	public ResponseEntity<Pet> getPetById(@PathVariable int petId) {
		Optional<Pet> petOpt = userServ.getPetById(petId);
		if (petOpt.isPresent()) {
			return ResponseEntity.ok(petOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping(path="/{petId}/adopt")
	public ResponseEntity<User> adoptPet(@PathVariable int petId, @RequestBody User user) {
		Optional<Pet> petOpt = userServ.getPetById(petId);
		
		try {
			if (petOpt.isPresent()) {
				Pet petToAdopt = petOpt.get();
				user = userServ.adoptPet(user, petToAdopt);
				return ResponseEntity.ok(user);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (AlreadyAdoptedException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}
