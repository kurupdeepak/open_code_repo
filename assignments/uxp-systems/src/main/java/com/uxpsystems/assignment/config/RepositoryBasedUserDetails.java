package com.uxpsystems.assignment.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.uxpsystems.assignment.dao.UserEntity;

public class RepositoryBasedUserDetails implements UserDetails {

	private UserEntity userEntity;
	
	public RepositoryBasedUserDetails(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	@Override
	public List<GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority(Authorities.ROLE_USER.toString()),
				new SimpleGrantedAuthority(Authorities.ROLE_ADMIN.toString())); 
	}

	@Override
	public String getPassword() {
		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return userEntity.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
