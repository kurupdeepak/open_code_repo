package com.uxpsystems.assignment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerIT extends AbstractUserTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	protected UserService userService;

	@Autowired
	protected UserRepository userRepository;

	@Before
	public void setup() {
		setUserService(userService);
		setRepository(userRepository);
		testUsers = createTestUsers();
	}

	@After
	public void cleanup() {
		deleteTestUsers();
	}

	@Test
	public void testGetWithUser() throws Exception {
		User user = getTestUser();
		ResponseEntity<String> userResponse = testRestTemplate
				.withBasicAuth(TEST_USER_NAME, TEST_USER_PASSWORD)
				.getForEntity("/user/" + user.getUserId(), String.class);
		assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.OK));
		JSONAssert.assertEquals(userResponse.getBody(), getUserJson(user), false);

	}

	@Test
	public void testGetWithUserNotExists() throws Exception {
		ResponseEntity<String> userResponse = testRestTemplate.withBasicAuth(TEST_USER_NAME, TEST_USER_PASSWORD)
				.getForEntity("/user/100", String.class);
		assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		JSONAssert.assertEquals(userResponse.getBody(),
				"{\"message\":\"User not found : 100\",\"errors\":null,\"status\":\"NOT_FOUND\"}", false);
	}

	@Test
	public void testUpdateWithUser() throws Exception {
		User user = getTestUser();

		ResponseEntity<String> userResponse = testRestTemplate.withBasicAuth(TEST_USER_NAME, TEST_USER_PASSWORD)
				.exchange("/user/" + user.getUserId(), HttpMethod.PUT, new HttpEntity<>(user, getHeaders()), String.class);

		assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
	}

	@Test
	public void testUpdateWithUserNotExists() throws Exception {
		User user = new User(100L, "tom@testdomain.com", "Tom Kirkman", "password", true);

		ResponseEntity<String> userResponse = testRestTemplate.withBasicAuth(TEST_USER_NAME, TEST_USER_PASSWORD)
				.exchange("/user/100", HttpMethod.PUT, new HttpEntity<>(user, getHeaders()), String.class);

		assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		
		JSONAssert.assertEquals(userResponse.getBody(),
				"{\"message\":\"User not found : 100\",\"errors\":null,\"status\":\"NOT_FOUND\"}", false);
	}

	@Test
	public void testDeleteUsers() {
		User user = getTestUser();

		ResponseEntity<String> userResponse = testRestTemplate.withBasicAuth(TEST_USER_NAME, TEST_USER_PASSWORD)
				.exchange("/user/" + user.getUserId(), HttpMethod.DELETE, new HttpEntity<>(user, getHeaders()), String.class);
		
		assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
	}

	@Test
	public void testPostWithUsers() throws Exception {
		User user = new User(null, "tom@testdomain.com", "Tom Kirkman", "password", true);

		ResponseEntity<String> userResponse = testRestTemplate.withBasicAuth(TEST_USER_NAME, TEST_USER_PASSWORD)
				.exchange("/user/", HttpMethod.POST, new HttpEntity<>(user, getHeaders()), String.class);
		
		assertThat(userResponse.getStatusCode(), equalTo(HttpStatus.CREATED));

		assertThat(getUserFromJson(userResponse.getBody()).getUserId(), is(notNullValue()));
	}

	
	protected HttpHeaders getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return httpHeaders;
	}
}
