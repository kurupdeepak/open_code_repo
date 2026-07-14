package com.uxpsystems.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uxpsystems.assignment.dao.UserEntity;
import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.exceptions.UserAlreadyExistsException;
import com.uxpsystems.assignment.service.exceptions.UserNotFoundException;
import com.uxpsystems.assignment.service.util.Mapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Mapper<UserEntity, User> mapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public UserServiceImpl() {}
	
	public UserServiceImpl(UserRepository userRepository, Mapper<UserEntity, User> mapper,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Get the user for a given id, if no user found throws an exception
	 */
	@Override
	@PreAuthorize("authenticated")
	public User getUser(Long userId) {
		UserEntity result = userRepository.findOne(userId);
		if (result == null)
			throw new UserNotFoundException("User not found : " + userId);
		return mapper.fromEntity(result);
	}

	/**
	 * Updates the user as a whole
	 */
	@Override
	@PreAuthorize("authenticated")
	public User updateUser(User user) {
		User currentCopyOfUser  = getUser(user.getUserId());
		
		if(!currentCopyOfUser.getPassword().equals(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return saveUser(copyOf(user));
	}
	
	protected User copyOf(User user) {
		User copiedUser = new User();
		copiedUser.setUserId(user.getUserId());
		copiedUser.setUserName(user.getUserName());
		copiedUser.setPassword(user.getPassword());
		copiedUser.setFullName(user.getFullName());
		copiedUser.setActive(user.isActive());
		return copiedUser;
	}
	
	/**
	 * Delete method to delete a user, its not a physical delete but deactivate the user
	 */
	
	@Override
	@PreAuthorize("authenticated")
	public User deActivateUser(Long userId) {
		User user  = getUser(userId);
		user.setActive(false);
		return saveUser(user);
	}
	
	/**
	 * Method which stores the entity state to the database
	 * @param user
	 * @return
	 */
	
	private User saveUser(User user) {
		UserEntity uE = userRepository.save(mapper.toEntity(user));
		return mapper.fromEntity(uE);
	}

	/**
	 * Used for creating a new user
	 */
	@Override
	@PreAuthorize("authenticated")
	public User createUser(User user) {
		if (isUserExist(user)) {
			throw new UserAlreadyExistsException("Cannot create !! User already exists ");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		UserEntity uE = mapper.toEntity(user);
		uE = userRepository.save(uE);
		return mapper.fromEntity(uE);
	}

	/**
	 * Checks if the user exists either by id or name
	 */
	@Override
	@PreAuthorize("authenticated")
	public boolean isUserExist(User user) {
		UserEntity userEntity = userRepository.findUserByUserIdOrUserName(user.getUserId(),user.getUserName());
		if(userEntity != null) {
			return true;
		}
		return false;
	}
	
}
