package com.uxpsystems.assignment.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.uxpsystems.assignment.AbstractUserTest;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.UserService;
import com.uxpsystems.assignment.service.exceptions.UserNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class TestUserControllerWithMock extends AbstractUserTest {
	
	@Autowired
	protected MockMvc mockMvc;

	@MockBean 
	protected UserService userService;
	
	@Test
	@WithMockUser(username = "john@testdomain.com", password = "password123")
	public void testGetWithMockUser() throws Exception {
		when(userService.getUser(MOCK_USER_ID)).thenReturn(createMockUser(true));
		mockMvc.perform(get("/user/" + MOCK_USER_ID)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andDo(print()).andExpect(content().json(getUserJson(createMockUser(true))));
		verify(userService).getUser(MOCK_USER_ID);
	}

	@Test
	@WithMockUser(username = "john@testdomain.com", password = "password123")
	public void testGetWithMockUserNotExists() throws Exception {
		when(userService.getUser(MOCK_USER_ID)).thenThrow(new UserNotFoundException("User not found : 100"));

		mockMvc.perform(get("/user/" + MOCK_USER_ID).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isNotFound())
				.andDo(print())
				.andExpect(content().json("{\"message\":\"User not found : 100\",\"errors\":null,\"status\":\"NOT_FOUND\"}"));

		verify(userService).getUser(MOCK_USER_ID);
	}

	@Test
	@WithMockUser(username = "john@testdomain.com", password = "password123")
	public void testUpdateWithMockUser() throws Exception {
		User mockUser = createMockUser(true);
		when(userService.updateUser(any(User.class))).thenReturn(mockUser);
		when(userService.getUser(MOCK_USER_ID)).thenReturn(mockUser);
		mockUser.setFullName("Tom Bundy");
		mockMvc.perform(put("/user/" + MOCK_USER_ID)
				.accept(MediaType.APPLICATION_JSON)
				.content(getUserJson(mockUser))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.fullName",is("Tom Bundy")));
	}
	
	@Test
	@WithMockUser(username = "john@testdomain.com", password = "password123")
	public void testUpdateWithMockUserNotExists() throws Exception {
		User user = createMockUser(true);
		user.setUserId(100L);
		user.setFullName("Tom Bundy");
		when(userService.updateUser(any(User.class))).thenThrow(new UserNotFoundException("User not found : 100"));//.thenReturn(user);
		mockMvc.perform(put("/user/100")
				.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(getUserJson(user))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isNotFound())
				.andExpect(content().json("{\"message\":\"User not found : 100\",\"errors\":null,\"status\":\"NOT_FOUND\"}"));
		verify(userService).updateUser(user);
	}

	@Test
	@WithMockUser(username = "john@testdomain.com", password = "password123")
	public void testPostWithMockUser() throws Exception {
		User mockUser = createMockUser(true);
		when(userService.createUser(any(User.class))).thenReturn(mockUser);
		mockMvc.perform(post("/user")
				.accept(MediaType.APPLICATION_JSON)
				.content(getUserJson(createMockUser(false)))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isCreated())
				.andExpect(content().json(getUserJson(mockUser)));
	}

	@Test
	@WithMockUser(username = "john@testdomain.com", password = "password123")
	public void testDeleteWithMockUser() throws Exception {
		User mockUser = createMockUser(true);
		mockUser.setActive(false);
		when(userService.deActivateUser(Mockito.anyLong())).thenReturn(mockUser);
		
		mockMvc.perform(delete("/user/" + MOCK_USER_ID)
				.accept(MediaType.APPLICATION_JSON)
				.content(getUserJson(mockUser))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.active",is(false)));
		verify(userService).deActivateUser(MOCK_USER_ID);
	}
	
	
	
}
