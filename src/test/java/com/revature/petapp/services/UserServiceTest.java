package com.revature.petapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.revature.petapp.PetAppBootApplication;
import com.revature.petapp.data.PetRepository;
import com.revature.petapp.data.StatusRepository;
import com.revature.petapp.data.UserRepository;
import com.revature.petapp.exceptions.IncorrectCredentialsException;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Status;
import com.revature.petapp.models.User;

@SpringBootTest(classes=PetAppBootApplication.class)
public class UserServiceTest {
	@MockBean
	private UserRepository userRepo;
	@MockBean
	private PetRepository petRepo;
	@MockBean
	private StatusRepository statusRepo;
	@Autowired
	private UserService userServ;

	@Test
	public void logInSuccessfully() throws IncorrectCredentialsException {
		// setup (arguments, expected result, etc.)
		String username = "snicholes";
		String password = "pass";
		
		// mocking: we need to mock userDao.getByUsername(username)
		// we're expecting a user with matching username & password
		User mockUser = new User();
		mockUser.setUsername(username);
		mockUser.setPassword(password);
		when(userRepo.findByUsername(username)).thenReturn(mockUser);
		
		// call the method we're testing
		User result = userServ.logIn(username, password);
		
		// assertion
		assertEquals(username, result.getUsername());
	}
	
	@Test
	public void logInWrongUsername() {
		String username = "abc123";
		String password = "1234567890";
		
		// we need to mock userDao.getByUsername(username)
		when(userRepo.findByUsername(username)).thenReturn(null);
		
		assertThrows(IncorrectCredentialsException.class, () -> {
			// put the code that we're expecting to throw the exception
			userServ.logIn(username, password);
		});
	}
	
	@Test
	public void logInWrongPassword() {
		String username = "snicholes";
		String password = "1234567890";
		
		User mockUser = new User();
		mockUser.setUsername(username);
		mockUser.setPassword("fake_password");
		when(userRepo.findByUsername(username)).thenReturn(mockUser);
		
		assertThrows(IncorrectCredentialsException.class, () -> {
			// put the code that we're expecting to throw the exception
			userServ.logIn(username, password);
		});
	}
	
	@Test
	public void registerSuccessfully() throws UsernameAlreadyExistsException {
		User newUser = new User();
		
		// mock userDao.create(newUser)
		User mockUser = new User();
		mockUser.setId(1);
		when(userRepo.save(newUser)).thenReturn(mockUser);
		
		User result = userServ.register(newUser);
		
		// the behavior that i'm looking for is that the
		// method returns the User with their newly generated ID,
		// so i want to make sure the ID was generated (not the default)
		assertNotEquals(0, result.getId());
	}
	
	@Test
	public void registerUsernameTaken() {
		User newUser = new User();
		newUser.setUsername("snicholes");
		
		// mock userDao.create(newUser)
		when(userRepo.save(newUser)).thenReturn(newUser);
		
		assertThrows(UsernameAlreadyExistsException.class, () -> {
			userServ.register(newUser);
		});
	}
	
	@Test
	public void viewPetsSuccessfully() {
		// mock petDao.getByStatus("Available");
		when(petRepo.findByStatusName("Available")).thenReturn(Collections.emptyList());
		
		List<Pet> pets = userServ.viewAvailablePets();
		
		// i just want to make sure that the pets are returned -
		// i don't need to check that the pets are all available
		// because that filtering happens in the database. i just
		// need to check that the pets list isn't null
		assertNotNull(pets);
	}
	
	@Test
	public void searchPetsBySpecies() {
		String species = "cat";
		
		// mock petDao.getAll()
		// i'm making a list that contains a pet with the species
		// and a pet without the species to make sure the service
		// is filtering them out properly
		List<Pet> mockPets = new LinkedList<>();
		Pet cat = new Pet();
		cat.setSpecies(species);
		Pet notCat = new Pet();
		notCat.setSpecies("dog");
		mockPets.add(cat);
		mockPets.add(notCat);
		
		when(petRepo.findAll()).thenReturn(mockPets);
		
		List<Pet> petsBySpecies = userServ.searchPetsBySpecies(species);
		
		boolean onlyCatsInList = true;
		for (Pet pet : petsBySpecies) {
			String petSpecies = pet.getSpecies().toLowerCase();
			// if the pet species doesn't contain the species passed in
			if (!petSpecies.contains(species)) {
				// then we'll set the boolean to false
				onlyCatsInList = false;
				// and stop the loop because we don't need to continue
				break;
			}
		}
		
		assertTrue(onlyCatsInList);
	}
	
	@Test
	public void adoptPetSuccessfully() throws Exception {
		User testUser = new User();
		Pet testPet = new Pet();
		
		// petDao.getById: return testPet
		when(petRepo.findById(testPet.getId())).thenReturn(Optional.of(testPet));
		// userDao.getById: return testUser
		when(userRepo.findById(testUser.getId())).thenReturn(Optional.of(testUser));
		
		Status mockAdoptedStatus = new Status();
		mockAdoptedStatus.setName("Adopted");
		when(statusRepo.findByName("Adopted")).thenReturn(mockAdoptedStatus);
		// petDao.update: do nothing
		// when petDao update is called with any pet object, do nothing
		when(petRepo.save(any(Pet.class))).thenReturn(testPet);
		// userDao.update: do nothing
		when(userRepo.save(any(User.class))).thenReturn(testUser);
		
		User result = userServ.adoptPet(testUser, testPet);
		
		// there are two behaviors i'm looking for:
		// that the user now has the pet in their list of pets,
		// and that the pet in the list has its status updated.
		// to check this, i'm checking that the pet with the Adopted
		// status is in the user's list.
		testPet.setStatus(mockAdoptedStatus);
		List<Pet> usersPets = result.getPets();
		assertTrue(usersPets.contains(testPet));
		
		// Mockito.verify allows you to make sure that a particular mock method
		// was called (or that it was never called, or how many times, etc.)
		verify(petRepo, times(1)).save(any(Pet.class));
	}
	
	@Test
	public void adoptPetAlreadyAdopted() throws SQLException {
		User testUser = new User();
		Pet testPet = new Pet();
		Status mockAdoptedStatus = new Status();
		mockAdoptedStatus.setName("Adopted");
		testPet.setStatus(mockAdoptedStatus);
		
		// petDao.getById: return testPet
		when(petRepo.getById(testPet.getId())).thenReturn(testPet);
		
		assertThrows(Exception.class, () -> {
			userServ.adoptPet(testUser, testPet);
		});
		
		verify(petRepo, never()).save(any(Pet.class));
		verify(userRepo, never()).save(any(User.class));
	}
}
