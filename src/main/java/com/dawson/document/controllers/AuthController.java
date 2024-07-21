package com.dawson.document.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawson.document.models.DeleteAccountRequest;
import com.dawson.document.models.DeleteAccountResponse;
import com.dawson.document.models.LoginRequest;
import com.dawson.document.models.LoginResponse;
import com.dawson.document.models.LogoutRequest;
import com.dawson.document.models.LogoutResponse;
import com.dawson.document.models.SignUpRequest;
import com.dawson.document.models.SignUpResponse;
import com.dawson.document.services.AuthService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/sign-up")
	public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
		log.info("Received Sign up request : {}", request);
		return ResponseEntity.ok(authService.signIn(request));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		log.info("Received login request : {}", request);
		return ResponseEntity.ok(authService.login(request));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
		log.info("Received logout request : {}", request);
		return ResponseEntity.ok(authService.logout(request));
	}
	
	@PostMapping("/delete-account")
	public ResponseEntity<DeleteAccountResponse> deleteAccount(@RequestBody DeleteAccountRequest request) {
		log.info("Received delete account request : {}", request);
		return ResponseEntity.ok(authService.deleteAccount(request));
	}
}
