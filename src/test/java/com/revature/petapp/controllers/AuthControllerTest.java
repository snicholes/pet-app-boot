package com.revature.petapp.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.petapp.PetAppBootApplication;
import com.revature.petapp.exceptions.IncorrectCredentialsException;
import com.revature.petapp.models.User;
import com.revature.petapp.services.UserService;

@SpringBootTest(classes=PetAppBootApplication.class)
public class AuthControllerTest {
	@MockBean
	private UserService userServ;
	@Autowired
	private AuthController authController;
	@Autowired
	private WebApplicationContext context;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void logInSuccessfully() throws JsonProcessingException, Exception {
		String mockCred = "test";
		
		Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("username", mockCred);
		credentials.put("password", mockCred);
		String credentialsJSON = jsonMapper.writeValueAsString(credentials);
		
		User mockUser = new User();
		mockUser.setUsername(mockCred);
		mockUser.setPassword(mockCred);
		when(userServ.logIn(mockCred, mockCred)).thenReturn(mockUser);
		
		mockMvc.perform(
				post("/auth")
				.content(credentialsJSON)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonMapper.writeValueAsString(mockUser)));
	}
}
