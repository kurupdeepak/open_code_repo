package com.uxpsystems.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.UserService;

@RestController("/user")
public class UserController {
	
	@Autowired 
	UserService userService;
	
	@GetMapping("/user/{id}")
	@ResponseStatus(code=HttpStatus.OK)	
	public User getUser(@PathVariable("id") Long id) {
		return userService.getUser(id);
	}
	
	@DeleteMapping("/user/{id}")
	@ResponseStatus(code=HttpStatus.ACCEPTED)
	public User deleteUser(@PathVariable("id") Long id) {
		return userService.deActivateUser(id);
	}
	
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}
	
	
	@PutMapping("/user/{id}")
	@ResponseStatus(code=HttpStatus.ACCEPTED)
	public User updateUser(@PathVariable Long id, @RequestBody User user) {
		user.setUserId(id);
		return userService.updateUser(user);
	}
}
