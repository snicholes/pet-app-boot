package com.revature.petapp.services;

import java.util.List;

import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.exceptions.IncorrectCredentialsException;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.User;

/*- here we can lay out all of the behaviors that we want
 * users to be able to do. services are just about the
 * services, or tasks, that we want to provide to users,
 * and the Java that makes those methods work.
 * 
 * it also allows for a separation between database code (DAOs),
 * HTTP handling code (Javalin), and the "business logic" or actual
 * functionality that users are doing (services). this idea is called
 * "separation of concerns" and allows you to have cleaner, more
 * organized, and more maintainable code.
 */
public interface UserService {
	/**
	 * returns the User if username and password are correct. 
	 * otherwise throws an IncorrectCredentialsException.
	 * 
	 * @param username
	 * @param password
	 * @return User matching the given username/password
	 */
	public User logIn(String username, String password) throws IncorrectCredentialsException;
	
	/**
	 * creates a new user. if the username is available, 
	 * returns the new user with their database-generated ID. 
	 * otherwise, throws a UsernameAlreadyExistsException.
	 * 
	 * @param newUser
	 * @return User with newly generated ID
	 */
	public User register(User newUser) throws UsernameAlreadyExistsException;
	
	/**
	 * 
	 * @return all available pets
	 */
	public List<Pet> viewAvailablePets();
	
	/**
	 * 
	 * @param species
	 * @return all available pets with the specified species
	 */
	public List<Pet> searchPetsBySpecies(String species);
	
	/**
	 * sets the pet to be adopted by the user. if the pet
	 * is not available, throws AlreadyAdoptedException.
	 * 
	 * @param user
	 * @param petToAdopt
	 * @return user with their newly adopted pet
	 * @throws Exception 
	 */
	public User adoptPet(User user, Pet petToAdopt) throws AlreadyAdoptedException, Exception;
	
	/**
	 * 
	 * @param id
	 * @return the pet with the specified ID
	 */
	public Pet getPetById(int id);
	
	/**
	 * 
	 * @param id
	 * @return the user with the specified ID
	 */
	public User getUserById(int id);
}
