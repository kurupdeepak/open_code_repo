package com.uxpsystems.assignment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.uxpsystems.assignment.dao.UserEntity;
import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.service.exceptions.UserNotFoundException;

@Component
public class RepositoryBasedUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
		UserEntity userEnt = userRepository.findUserByUserName(user);
		if(userEnt == null) {
			throw new UserNotFoundException("User does not exist : " + user);
		}
		return new RepositoryBasedUserDetails(userEnt);
	}

}
