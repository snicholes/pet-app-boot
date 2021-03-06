package com.revature.petapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PetAppBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetAppBootApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedMethods("GET", "OPTIONS", "PUT", "POST", "DELETE", "PATCH")
					.allowedOrigins("http://localhost:4200",
						"http://sierra-petapp-ng.s3-website.us-east-2.amazonaws.com")
					.allowedHeaders("*")
					.allowCredentials(false);
			}
		};
	}

}
