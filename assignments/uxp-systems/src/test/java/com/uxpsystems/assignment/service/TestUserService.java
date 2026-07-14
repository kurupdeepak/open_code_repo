package com.uxpsystems.assignment.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.AbstractUserTest;
import com.uxpsystems.assignment.dao.UserEntity;
import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.exceptions.UserAlreadyExistsException;
import com.uxpsystems.assignment.service.exceptions.UserNotFoundException;
import com.uxpsystems.assignment.service.util.Mapper;

@RunWith(SpringRunner.class)
@ContextConfiguration
@ComponentScan(basePackages = { "com.uxpsystems.assignment"})
public class TestUserService extends AbstractUserTest{
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Mapper<UserEntity,User> userMapper;
	
	@Spy
	PasswordEncoder passwordEncoder;

	@Before
	public void setup() {
	}
	
	/**
	 * Tests get existing user on service mocking repository and mapper
	 */
	
	@Test
	@WithMockUser
	public void testGetUser() {
		when(userRepository.findOne(MOCK_USER_ID)).thenReturn(createMockUserEntity(true));
		when(userMapper.fromEntity(Mockito.any(UserEntity.class))).thenReturn(createMockUser(true));
		User result = userService.getUser(MOCK_USER_ID);
		Assert.assertThat(result.getUserName(),is(MOCK_USER_NAME));
	}

	@Test(expected=UserNotFoundException.class)
	public void testGetUserNotFound() throws Exception {
		when(userRepository.findOne(MOCK_USER_ID)).thenReturn(createMockUserEntity(true));
		userService.getUser(100L);
		verify(userRepository).findOne(100L);
	}
	
	@Test
	public void testUpdateUser() {
		UserEntity usrEnt = createMockUserEntity(true);
		User user = createMockUser(true);
		usrEnt.setFullName("Tom Bundy");
		user.setFullName("Tom Bundy");
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(usrEnt);
		when(userRepository.findOne(Mockito.any(Long.class))).thenReturn(usrEnt);
		when(userMapper.fromEntity(Mockito.any(UserEntity.class))).thenReturn(user);
		when(userMapper.toEntity(Mockito.any(User.class))).thenReturn(usrEnt);
		User result = userService.updateUser(user);
		Assert.assertThat(result.getFullName(),is("Tom Bundy"));
	}
	
	/**
	 * Checks if User Does not exist return UserNotFoundException
	 */
	
	@Test(expected=UserNotFoundException.class)
	public void testUpdateUserWithoutId() {
		UserEntity usrEnt = createMockUserEntity(false);
		usrEnt.setUserId(100L);
		usrEnt.setPassword("pass123");
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(usrEnt);
		userService.updateUser(createMockUser(true));
		verify(userRepository).save(new UserEntity());
	}

	/**
	 * Check if the user is deactivated
	 */
	@Test
	public void testDeleteUser() {
		UserEntity usrEnt = createMockUserEntity(true);

		UserEntity savedEntity = createMockUserEntity(true);
		
		savedEntity.setActive(false);

		when(userRepository.findOne(Mockito.anyLong())).thenReturn(usrEnt);
		
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(savedEntity);
		
		User user = createMockUser(true);
		user.setActive(false);
		
		when(userMapper.fromEntity(Mockito.any(UserEntity.class))).thenReturn(user);
		when(userMapper.toEntity(Mockito.any(User.class))).thenReturn(savedEntity);
		
		User result = userService.deActivateUser(MOCK_USER_ID);
		
		verify(userRepository).save(savedEntity);
		
		verify(userRepository).findOne(savedEntity.getUserId());
		
		Assert.assertThat(result.isActive(),is(false));
	}
	
	/**
	 * Tests if the UserNotFoundException is called for if user does not exists.
	 */
	
	@Test(expected=UserNotFoundException.class)
	public void testDeleteUserDoNotExist() {
		UserEntity usrEnt = createMockUserEntity(true);
		when(userRepository.findOne(Mockito.anyLong())).thenReturn(null);
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(usrEnt);
		usrEnt.setActive(false);
		userService.deActivateUser(100L);
		verify(userRepository).findOne(100L);
	}
	/**
	 * Test if user is created successfully
	 */
	@Test
	public void testCreateUser() {
		UserEntity usrEnt = createMockUserEntity(false);
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(usrEnt);
		when(userMapper.fromEntity(Mockito.any(UserEntity.class))).thenReturn(createMockUser(true));
		when(userMapper.toEntity(Mockito.any(User.class))).thenReturn(createMockUserEntity(true));
		User user = userService.createUser(createMockUser(false));
		Assert.assertThat(user,is(notNullValue()));
	}
	/**
	 * Tests create user if user already exists throws UserAlreadyExistsException
	 */
	
	@Test(expected=UserAlreadyExistsException.class)
	public void testCreateUserAlreadyExists() {
		UserEntity userEntity = createMockUserEntity(true);
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
		when(userRepository.findUserByUserIdOrUserName(null, MOCK_USER_NAME)).thenReturn(userEntity);
		User user = createMockUser(false);
		user = userService.createUser(user);
		verify(userRepository).findUserByUserIdOrUserName(null,MOCK_USER_NAME);
		verify(userRepository).save(userEntity);
		Assert.assertThat(user,is(notNullValue()));
		
	}
	
	@Test
	public void testIsUserExist() {
		when(userRepository.findUserByUserIdOrUserName(MOCK_USER_ID, MOCK_USER_NAME)).thenReturn(createMockUserEntity(true));
		boolean exists = userService.isUserExist(createMockUser(true));
		Assert.assertThat(exists,is(true));
	}
	
	@Test
	public void testIsUserNotExist() {
		when(userRepository.findUserByUserIdOrUserName(100L, "tom@testdomain.com")).thenReturn(Optional.of(createMockUserEntity(false)).get());
		boolean exists = userService.isUserExist(createMockUser(true));
		Assert.assertThat(exists,is(false));
	}
	
	@Configuration
	@ComponentScan(basePackages = { "com.uxpsystems.assignment"})
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled=true)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public static class TestUserConfiguration  extends GlobalMethodSecurityConfiguration{
		private static Logger log = LoggerFactory.getLogger(TestUserConfiguration.class);
		
		@Bean
		public ObjectMapper jsonOM() {
			return new ObjectMapper();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			log.debug("Configure called ");
			PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			auth.inMemoryAuthentication()
			.passwordEncoder(pe)
			.withUser("user")
			.password(pe.encode("password"))
			.roles("USER")
			.and()
			.withUser("admin")
			.password(pe.encode("password"))
			.roles("ADMIN", "USER");
		}
		
	}

}
