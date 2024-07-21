package com.dawson.document.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawson.document.entities.User;
import com.dawson.document.jwt.JWTTokenService;
import com.dawson.document.models.DeleteAccountRequest;
import com.dawson.document.models.DeleteAccountResponse;
import com.dawson.document.models.LoginRequest;
import com.dawson.document.models.LoginResponse;
import com.dawson.document.models.LogoutRequest;
import com.dawson.document.models.LogoutResponse;
import com.dawson.document.models.SignUpRequest;
import com.dawson.document.models.SignUpResponse;
import com.dawson.document.repositories.DocumentRepository;
import com.dawson.document.repositories.JWTRepository;
import com.dawson.document.repositories.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JWTTokenService jwtTokenService;
	@Autowired
	private JWTRepository jwtRepository;
	@Autowired
	private DocumentRepository documentRepository;

	public SignUpResponse signIn(SignUpRequest request) {
		try {
			ArrayList<String> roles = new ArrayList<String>();
			roles.add("EDITOR");
			User user = User.builder()
							.id(request.getUsername())
							.email(request.getEmail())
							.password(request.getPassword())
							.username(request.getUsername())
							.roles(roles)
							.documents(new HashMap<String, String>())
							.build();
			userRepository.save(user);
			log.info("Sign up Successfull");
			return SignUpResponse.builder().message("Success").status("200").build();
		} catch (Exception e) {
			log.info("Exception while signing up user");
			throw new RuntimeException();
		}
	}

	public LoginResponse login(LoginRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getUsername());
			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException();
			}
			if (!user.get().getPassword().equals(request.getPassword())) {
				log.info("Invalid password");
				throw new RuntimeException();
			}
			log.info("Login Successfull");
			String token = jwtTokenService.generateToken(request.getUsername());
			return LoginResponse.builder()
								.status("200")
								.message("Login Success")
								.token(token)
								.build();
		} catch (Exception e) {
			log.error(e);
			log.info("Exception while logging in user");
			throw new RuntimeException();

		}
	}
	
	public LogoutResponse logout(LogoutRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getUsername());
			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException();
			}
			jwtRepository.deleteById(request.getUsername());
			log.info("Logout Successfull");
			return LogoutResponse.builder()
					.status("200")
					.message("Logout Success")
					.build();
		} catch (Exception e) {
			log.info("Exception while logging out user");
			throw new RuntimeException();
			
		}
	}
	
	public DeleteAccountResponse deleteAccount(DeleteAccountRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getUsername());
			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException();
			}
			Set<String> documents = user.get().getDocuments().keySet();
			jwtRepository.deleteById(request.getUsername());
			userRepository.deleteById(request.getUsername());
			documentRepository.deleteAllById(documents);
			log.info("Account deleted Successfully");
			return DeleteAccountResponse.builder()
					.status("200")
					.message("Delete Account Success")
					.build();
		} catch (Exception e) {
			log.info("Exception while deleting user account");
			throw new RuntimeException();
			
		}
	}
}
