package com.uxpsystems.assignment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.UserServiceImpl;
import com.uxpsystems.assignment.service.exceptions.UserAlreadyExistsException;
import com.uxpsystems.assignment.service.exceptions.UserNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIT extends AbstractUserTest{
	private static Logger log = LoggerFactory.getLogger(UserServiceIT.class);
	
	@Autowired
	UserServiceImpl userService;
	
	@Autowired 
	UserRepository userRepository;

	@Before
	public void setup() {
		setUserService(userService);
		setRepository(userRepository);
		if(testUsers == null)
		testUsers = createTestUsers();
	}

	
	@After
	public void cleanup() {
		deleteTestUsers();
		testUsers=null;
	}	

	
	/**
	 * Tests get existing user on service mocking repository and mapper
	 */
	
	@Test
	@WithMockUser
	public void testGetUser() {
		User user = getTestUser();
		if(user == null)
			fail("Initialization failed");
		User resultUser = userService.getUser(user.getUserId());
		Assert.assertThat(resultUser, is(user));
	}
	
	
	/**
	 * Tests if the method on service is called with authenticated user.
	 */
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)	
	public void testAuthenticationForGetUser() {
		userService.getUser(getTestUser().getUserId());
	}

	@Test(expected=UserNotFoundException.class)
	@WithMockUser
	public void testGetUserNotFound() throws Exception {
		userService.getUser(100L);
	}
	
	@Test
	@WithMockUser
	public void testUpdateUser() {
		User user = getTestUser();
		user.setFullName("Tom Bundy");
		User resultUser = userService.updateUser(user);
		Assert.assertThat(resultUser.getFullName(),is("Tom Bundy"));
	}
	
	/**
	 * Checks if User Does not exist return UserNotFoundException
	 */
	
	@Test(expected=UserNotFoundException.class)
	@WithMockUser
	public void testUpdateUserWithoutId() {
		User user = getTestUser();
		user.setUserId(100L);
		user.setFullName("Tom Bundy");
		userService.updateUser(user);
	}

	/**
	 * Check if the user is deactivated
	 */
	@Test
	@WithMockUser
	public void testDeleteUser() {
		User resultUser = userService.deActivateUser(getTestUser().getUserId());
		Assert.assertThat(resultUser.isActive(),is(false));
	}
	
	/**
	 * Tests if the UserNotFoundException is called for if user does not exists.
	 */
	
	@Test(expected=UserNotFoundException.class)
	@WithMockUser
	public void testDeleteUserDoNotExist() {
		userService.deActivateUser(100L);
	}

	/**
	 * Test if user is created successfully
	 */
	@Test
	@WithMockUser
	public void testCreateUser() {
		User user = userService.createUser(createMockUser(false));
		Assert.assertThat(user.getUserId(),is(notNullValue()));
	}
	/**
	 * Tests create user if user already exists throws UserAlreadyExistsException
	 */
	
	@Test(expected=UserAlreadyExistsException.class)
	@WithMockUser
	public void testCreateUserAlreadyExists() {
		User user = userService.createUser(getTestUser());
	}
	
	@Test
	@WithMockUser
	public void testIsUserExist() {
		boolean exists = userService.isUserExist(getTestUser());
		Assert.assertThat(exists,is(true));
	}
	
	@Test
	@WithMockUser
	public void testIsUserNotExist() {
		User user  = new User(1L,"user100@testdomain.com","Aaron Show 1","password1",true);
		boolean exists = userService.isUserExist(user);
		Assert.assertThat(exists,is(false));
	}
}
