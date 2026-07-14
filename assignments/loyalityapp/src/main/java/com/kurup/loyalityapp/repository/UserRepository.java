package com.kurup.loyalityapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.kurup.loyalityapp.model.UserModel;

public interface UserRepository extends CrudRepository<UserModel,Integer>{

}
