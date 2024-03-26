package com.example.cms.utility;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.cms.exception.UserAlreadyExistByEmailException;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	
	private ErrorStructure<String> errorStructure;

	
	private ResponseEntity<ErrorStructure<String>> errorResponse(HttpStatus status , String errorMessage , String rootCause) {
		return new ResponseEntity<ErrorStructure<String>> (errorStructure.setStatuscode(status.value())
				.setErrorMessage(errorMessage)
				.setRootCause(rootCause), status);
	}
	
	
	
	public ResponseEntity<ErrorStructure<String>> handelUserAlreadyExistByEmail (UserAlreadyExistByEmailException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "User Already Exist with the given email id");
	}

}
