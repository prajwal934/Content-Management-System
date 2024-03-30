package com.example.cms.utility;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.cms.exception.UserAlreadyExistByEmailException;
import com.example.cms.exception.UserNotFoundByIdException;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	
	private ErrorStructure errorStructure;

	
	private ResponseEntity<ErrorStructure> errorResponse(HttpStatus status , String errorMessage , Object rootCause) {
		return new ResponseEntity<ErrorStructure> (errorStructure.setStatuscode(status.value())
				.setErrorMessage(errorMessage)
				.setRootCause(rootCause), status);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundByIdException(UserNotFoundByIdException ex) {
		errorStructure.setStatuscode(HttpStatus.BAD_REQUEST.value());
		errorStructure.setErrorMessage(ex.getMessage());
		errorStructure.setRootCause("please enter a valid userId");
		return new ResponseEntity<ErrorStructure>(errorStructure, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> handelUserAlreadyExistByEmail (UserAlreadyExistByEmailException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "User Already Exist with the given email id");
	}

}
