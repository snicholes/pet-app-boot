package com.revature.petapp.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByUsername(String username);
	// public User findByUsernameAndPassword(String username, String password);
	// public List<User> findByRoleName(String roleName);
}
