package com.revature.petapp.services;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.petapp.data.PetRepository;
import com.revature.petapp.data.StatusRepository;
import com.revature.petapp.data.UserRepository;
import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.exceptions.IncorrectCredentialsException;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Status;
import com.revature.petapp.models.User;

// after creating our SpringData JPA repositories (DAOs),
// we just need to update the methods
// getBy/getAll -> findBy/findAll
// create/update -> save
// getById(id) -> findById(id).get()
// delete -> delete

@Service
public class UserServiceImpl implements UserService {
	// field injection
	//@Autowired
	private UserRepository userRepo;
	//@Autowired
	private PetRepository petRepo;
	private StatusRepository statusRepo;
	
	// constructor injection
	@Autowired // can skip the annotation if you only have one constructor
	public UserServiceImpl(UserRepository userRepo, PetRepository petRepo,
			StatusRepository statusRepo) {
		this.userRepo = userRepo;
		this.petRepo = petRepo;
		this.statusRepo = statusRepo;
	}
	
	// setter injection
//	@Autowired
//	public void setUserDao(UserDAO userDao) {
//		this.userDao = userDao;
//	}

	@Override
	public User logIn(String username, String password) throws IncorrectCredentialsException {
		User user = userRepo.findByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		} else {
			throw new IncorrectCredentialsException();
		}
	}

	@Override
	public User register(User newUser) throws UsernameAlreadyExistsException {
		int id = userRepo.save(newUser).getId();
		if (id != 0) {
			newUser.setId(id);
			return newUser;
		} else {
			throw new UsernameAlreadyExistsException();
		}
	}

	@Override
	public List<Pet> viewAvailablePets() {
		return petRepo.findByStatusName("Available");
	}

	@Override
	public List<Pet> searchPetsBySpecies(String species) {
		List<Pet> pets = petRepo.findAll();
		
		List<Pet> petsWithSpecies = new LinkedList<>();
		for (int i=0; i<pets.size(); i++) {
			// if the pet's species is equal to the species passed in
			// (toLowerCase to allow it to be case-insensitive)
			if (pets.get(i).getSpecies().toLowerCase().equals(species.toLowerCase())) {
				petsWithSpecies.add(pets.get(i));
			}
		}
		
		return petsWithSpecies;
	}

	@Override
	// tells Spring Data that the multiple DML statements in this method
	// are part of a single transaction to enforce ACID properties
	@Transactional
	public User adoptPet(User user, Pet petToAdopt) throws AlreadyAdoptedException, Exception {
		petToAdopt = petRepo.findById(petToAdopt.getId()).get();
		
		if (petToAdopt.getStatus().getName().equals("Adopted")) {
			throw new AlreadyAdoptedException();
		} else {
			Optional<User> userOpt = getUserById(user.getId());
			if (userOpt.isPresent()) {
				user = userOpt.get();
				// proceed with adopting
				Status adoptedStatus = statusRepo.findByName("Adopted");
				petToAdopt.setStatus(adoptedStatus);
				user.getPets().add(petToAdopt);
				petRepo.save(petToAdopt);
				userRepo.save(user);
			}
			return user;
		}
	}

	@Override
	public Optional<Pet> getPetById(int id) {
		return petRepo.findById(id);
	}
	
	@Override
	public Optional<User> getUserById(int id) {
		Optional<User> userOpt = userRepo.findById(id);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setFullName(user.getFullName());
			return Optional.of(user);
		}
		return userOpt;
	}
}
