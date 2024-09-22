package com.dawson.document.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dawson.document.models.ErrorResponse;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handle(Exception e) {
		return ResponseEntity.ok(ErrorResponse.builder().message(e.getMessage()).status("500").build());

	}

}
