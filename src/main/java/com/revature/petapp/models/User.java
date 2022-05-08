package com.revature.petapp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="person") // this annotation is only necessary when the name is different
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	@Column(name="passwd")
	private String password;
	@Transient // these fields don't exist in the database
	private String firstName;
	@Transient
	private String lastName;
	private String fullName;
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	@OneToMany
	@JoinTable(name="pet_owner",
		joinColumns=@JoinColumn(name="owner_id"), // foreign key to the current class
		inverseJoinColumns=@JoinColumn(name="pet_id")) // fk to the field class
	private List<Pet> pets;
	
	public User() {
		id=0;
		username="";
		password="";
		firstName="";
		lastName="";
		fullName="";
		role = new Role();
		pets = new ArrayList<>();
	}

	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
		if (fullName.contains(" ")) {
			setFirstName(fullName.substring(0, fullName.indexOf(' ')));
			setLastName(fullName.substring(fullName.indexOf(' ') + 1));
		} else {
			setFirstName(fullName);
		}
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", fullName=" + fullName + ", role=" + role + ", pets=" + pets + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, fullName, id, lastName, password, pets, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(firstName, other.firstName) && Objects.equals(fullName, other.fullName) && id == other.id
				&& Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password)
				&& Objects.equals(pets, other.pets) && Objects.equals(role, other.role)
				&& Objects.equals(username, other.username);
	}
}
