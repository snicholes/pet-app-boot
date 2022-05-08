package com.revature.petapp.exceptions;

public class IncorrectCredentialsException extends Exception {

	public IncorrectCredentialsException() {
		super("The username and/or password were incorrect.");
	}
}
