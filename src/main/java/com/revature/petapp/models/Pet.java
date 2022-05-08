package com.revature.petapp.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column // the column annotation is unnecessary if the name is the same
	private int id;
	@Column
	private String name;
	@Column
	private int age;
	@Column
	private String species;
	@Column
	private String description;
	@ManyToOne
	@JoinColumn(name="status_id")
	private Status status;
	
	public Pet() {
		id = 0;
		name = "";
		age = 0;
		species = "";
		description = "";
		status = new Status();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Pet [id=" + id + ", name=" + name + ", age=" + age + ", species=" + species + ", description="
				+ description + ", status=" + status + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(age, description, id, name, species, status);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pet other = (Pet) obj;
		return age == other.age && Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(name, other.name) && Objects.equals(species, other.species)
				&& Objects.equals(status, other.status);
	}

}
