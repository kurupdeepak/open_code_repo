package com.uxpsystems.assignment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.uxpsystems.assignment.service.exceptions.UserAlreadyExistsException;
import com.uxpsystems.assignment.service.exceptions.UserNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler({ Exception.class })
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public AppError handleAll(Exception ex, WebRequest request) {
	    return new AppError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "Oops!Not behaving as expected");
	}
	
	@ExceptionHandler({ UserNotFoundException.class })
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public AppError handleNotFound(UserNotFoundException ex, WebRequest request) {
	    return new AppError(HttpStatus.NOT_FOUND, ex.getMessage());
	}
	
	@ExceptionHandler({ UserAlreadyExistsException.class })
	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	public AppError handleUserExists(UserAlreadyExistsException ex, WebRequest request) {
	    return new AppError(HttpStatus.CONFLICT, ex.getMessage());
	}
}
