package com.dawson.document.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Allow all endpoints to be accessed
						.allowedOrigins("http://localhost:3000", "http://localhost:3000/") // Add your allowed origins here
						.allowedMethods("GET", "POST", "PUT", "DELETE") // Add your allowed methods here
						.allowedHeaders("*") // Allow all headers
						.allowCredentials(true) // Allow credentials (cookies, authorization headers, etc.)
						.maxAge(3600); // Cache the preflight response for 3600 seconds
			}
		};
	}
}
