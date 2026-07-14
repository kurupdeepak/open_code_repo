package com.uxpsystems.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uxpsystems.assignment.dao.UserEntity;
import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.util.Mapper;

@Component 
public class DefaultUserLoader implements ApplicationRunner{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired 
	private Mapper<UserEntity,User> userMapper;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		User user = new User(null,"systemuser@testdomain.com","Sys Admin",encoder.encode("password1"),true);
		userRepository.save(userMapper.toEntity(user));
	}
}

