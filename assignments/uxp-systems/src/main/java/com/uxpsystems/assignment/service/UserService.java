package com.uxpsystems.assignment.service;

import java.util.List;

import com.uxpsystems.assignment.model.User;

public interface UserService {

	User getUser(Long userId);

	User updateUser(User user);

	User deActivateUser(Long id);

	User createUser(User user);
	
	boolean isUserExist(User user);

}