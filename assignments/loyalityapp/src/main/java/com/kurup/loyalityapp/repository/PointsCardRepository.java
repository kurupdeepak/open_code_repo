package com.kurup.loyalityapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kurup.loyalityapp.model.PointCardModel;

public interface PointsCardRepository extends CrudRepository<PointCardModel,Integer>{

	@Query("select p from PointCardModel p where p.user.userId =?1")
	List<PointCardModel> findCardsForUser(int userId);
	
	@Query("select p from PointCardModel p where p.cardNumber =?1")
	PointCardModel findByCardNumber(String cardNumber);
}
