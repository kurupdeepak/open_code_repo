package com.uxpsystems.assignment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findUserByUserName(String userName);
	
	UserEntity findUserByUserIdOrUserName(Long userId,String userName);
}
