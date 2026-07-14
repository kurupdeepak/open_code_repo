package com.uxpsystems.assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.controller.AppError;
import com.uxpsystems.assignment.dao.UserEntity;
import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.UserService;
import com.uxpsystems.assignment.service.util.Mapper;
import com.uxpsystems.assignment.service.util.UserMapper;

public abstract class AbstractUserTest {
	Logger log = LoggerFactory.getLogger(this.getClass());

	protected static final Long MOCK_USER_ID = 1L;

	protected static final String MOCK_USER_NAME = "john@testdomain.com";

	protected static final String MOCK_PASSWORD = "password123";

	protected static final String MOCK_USER_FULL_NAME = "John Sheffer";

	protected static final String TEST_USER_NAME = "user1@testdomain.com";

	protected static final String TEST_USER_PASSWORD = "password1";

	protected @Spy ObjectMapper jsonMapper;

	protected Map<Long, User> testUsers;

	protected UserRepository repository;

	protected UserService userService;

	protected UserEntity createMockUserEntity(boolean withId) {
		UserEntity user = new UserEntity();
		if (withId) {
			user.setUserId(MOCK_USER_ID);
		}
		user.setUserName(MOCK_USER_NAME);
		user.setPassword(MOCK_PASSWORD);
		user.setFullName(MOCK_USER_FULL_NAME);
		user.setActive(true);
		return user;
	}

	protected User createMockUser(boolean withId) {
		User user = new User();

		if (withId)
			user.setUserId(MOCK_USER_ID);
		user.setFullName(MOCK_USER_FULL_NAME);
		user.setUserName(MOCK_USER_NAME);
		user.setPassword(MOCK_PASSWORD);
		user.setActive(true);
		return user;
	}

	protected String getUserJson(User user) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(user);
	}

	protected String getErrorJson(AppError error) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(error);
	}

	protected User getUserFromJson(String json) throws IOException {
		return jsonMapper.readValue(json, User.class);
	}

	public UserRepository getRepository() {
		return repository;
	}

	public void setRepository(UserRepository repository) {
		this.repository = repository;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	protected Map<Long, User> createTestUsers() {
		testUsers = new HashMap<>();
		Mapper<UserEntity, User> userMapper = new UserMapper();
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		List<UserEntity> tempUsers = new ArrayList<>();
		for (int i = 1; i < 3; i++) {
			UserEntity userEntity = userMapper.toEntity(new User(null, "user" + i + "@testdomain.com",
					"Aaron Show " + i, encoder.encode("password" + i), true));
			tempUsers.add(userEntity);
			log.debug("User going to be created " + userEntity);
		}

		List<UserEntity> results = repository.save(tempUsers);

		repository.flush();

		results.stream().forEach(usrEnt -> {
			User user = userMapper.fromEntity(usrEnt);
			testUsers.put(user.getUserId(), user);
		});

		return testUsers;
	}

	protected void deleteTestUsers() {
		if (testUsers != null) {
			repository.deleteAll();
		}
		testUsers.clear();
	}

	protected void showAllTestUsers() {
		if (testUsers != null) {
			repository.findAll().stream().forEach(u -> log.debug("Users Created " + u.toString()));
		}
	}

	protected User getTestUser() {
		Optional<Long> userId = testUsers.keySet().stream().findAny();

		if (testUsers != null && userId.isPresent()) {
			return testUsers.get(userId.get());
		} else {
			throw new UsernameNotFoundException("Initialization failed, cannot create any test user");
		}
	}

}
