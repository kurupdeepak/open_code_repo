package com.uxpsystems.assignment.service.util;

import org.springframework.stereotype.Component;

import com.uxpsystems.assignment.dao.UserEntity;
import com.uxpsystems.assignment.model.User;

@Component
public class UserMapper implements Mapper<UserEntity,User> {
	@Override
	public User fromEntity(UserEntity uE) {
		User u = new User();
		u.setActive(uE.isActive());
		u.setUserId(uE.getUserId());
		u.setUserName(uE.getUserName());
		u.setFullName(uE.getFullName());
		u.setPassword(uE.getPassword());
		return u;

	}

	@Override
	public UserEntity toEntity(User user) {
		UserEntity uE = new UserEntity();
		uE.setActive(user.isActive());
		uE.setUserId(user.getUserId());
		uE.setUserName(user.getUserName());
		uE.setPassword(user.getPassword());
		uE.setFullName(user.getFullName());
		return uE;
	}

}
