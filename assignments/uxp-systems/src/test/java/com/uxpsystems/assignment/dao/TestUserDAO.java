package com.uxpsystems.assignment.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.uxpsystems.assignment.AbstractUserTest;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestUserDAO extends AbstractUserTest{

	@Autowired
	UserRepository userRepository;
	
	@Before
	public void setup() {
		userRepository.save(createMockUserEntity(true));
	}
	
	@After
	public void cleanup() {
		userRepository.deleteAll();
	}
	
	@Test
	public void testFindUserByUserName() {
		UserEntity user = userRepository.findUserByUserName(MOCK_USER_NAME);
		Assert.assertThat(user, is(notNullValue()));
	}

	@Test
	public void testIfExistsFindUserByUserIdOrUserName() {
		UserEntity user = userRepository.findUserByUserIdOrUserName(MOCK_USER_ID, MOCK_USER_NAME);
		Assert.assertThat(user, is(notNullValue()));
	}
	
	@Test
	public void testIfDoNotExistsFindUserByUserIdOrUserName() {
		UserEntity user = userRepository.findUserByUserIdOrUserName(100L, "testuser@testdomain.com");
		Assert.assertThat(user, is(nullValue()));
	}
	

}
