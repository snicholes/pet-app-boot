package com.revature.petapp.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Status;

// usually stereotypes go over classes, but in this case, it goes over the interface
// because we will not be writing the classes (spring data will)
@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
	// if you follow a particular pattern with your abstract method names,
	// spring data will implement them for you
	public List<Pet> findByStatusName(String statusName);
}
