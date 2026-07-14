package com.uxpsystems.assignment.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class AppError {
	private String message;
	private List<String> errors;
	private HttpStatus status;
	
	public AppError(HttpStatus status, String msg, List<String> err) {
		setStatus(status);
		setMessage(msg);
		setErrors(err);
	}
	
	public AppError(HttpStatus status, String msg, String err) {
		setStatus(status);
		setMessage(msg);
		setErrors(Arrays.asList(err));
	}
	
	public AppError(HttpStatus status, String msg) {
		setStatus(status);
		setMessage(msg);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	
}
