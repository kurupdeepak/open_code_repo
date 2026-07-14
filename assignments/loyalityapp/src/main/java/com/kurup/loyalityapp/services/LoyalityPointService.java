package com.kurup.loyalityapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kurup.loyalityapp.model.PointCardModel;
import com.kurup.loyalityapp.model.UserModel;
import com.kurup.loyalityapp.repository.PointsCardRepository;
import com.kurup.loyalityapp.repository.UserRepository;
import com.kurup.loyalityapp.util.ServiceUtil;

@Service
public class LoyalityPointService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired 
	PointsCardRepository pointCardRepository;
	
	@Autowired
	PointCardNumberGenerator pointCardNumberGenerator;
	
	public PointCardModel addCard(UserModel user) {
		PointCardModel pointCard = new PointCardModel();
		pointCard.setUser(user);
		pointCard.setStatus(CardStatus.ACTIVE.getState());
		pointCard.setCardNumber(pointCardNumberGenerator.generateNumber());
		return pointCardRepository.save(pointCard);
	}
	
	public void cancelCard(PointCardModel pointCard) {
		pointCard.setStatus(CardStatus.CANCELLED.getState());
		pointCardRepository.save(pointCard);
	}
	
	public PointCardModel updatePoints(PointCardModel pointCard,long newPoints) {
		pointCard.setPoints(pointCard.getPoints() + newPoints);
		return pointCardRepository.save(pointCard);
	}

	public List<UserModel> getUsers(){
		List<UserModel> list = new ArrayList<>();
		userRepository.findAll().forEach(e -> list.add(e));
		return list;
	}
	
	public List<PointCardModel> getCards(UserModel user){
		return pointCardRepository.findCardsForUser(user.getUserId());
	}
	
	public PointCardModel getCardDetails(String cardNumber){
		ServiceUtil.isValidCardNumber(cardNumber,"Invalid card number " + cardNumber);
		return pointCardRepository.findByCardNumber(cardNumber);
	}

	public UserModel getUser(int userId) {
		Optional<UserModel> user = userRepository.findById(userId);
		if (user.isPresent())
			return user.get();
		return null;
	}

	public UserModel createUser(UserModel model) {
		ServiceUtil.validateParameterNotNull(model.getName(),"Name should not be null");
		return userRepository.save(model);
	}

	public void deleteUser(int userId) {
		userRepository.deleteById(userId);
	}
	
}
