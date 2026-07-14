package com.kurup.loyalityapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurup.loyalityapp.exception.CardNotFoundException;
import com.kurup.loyalityapp.exception.UserNotFoundException;
import com.kurup.loyalityapp.model.PointCardModel;
import com.kurup.loyalityapp.model.UserModel;
import com.kurup.loyalityapp.services.LoyalityPointService;

@RestController
public class LoyalityAppController {

	@Autowired 
	LoyalityPointService loyalityPointService;
	
	@GetMapping("/users")
	public List<UserModel> getUsers(){
		return loyalityPointService.getUsers();
	}


	@DeleteMapping("/users/{userId}")
	public  void deleteUser(@PathVariable int userId){
		loyalityPointService.deleteUser(userId);		
	}
	
	@PostMapping("/users")
	public UserModel createUser(UserModel userModel,@RequestParam(required=false) String name){
		if(name == null) {
			return loyalityPointService.createUser(userModel);
		}
		UserModel user = new UserModel();
		user.setName(name);
		return loyalityPointService.createUser(user);		
	}
	
	@GetMapping("/users/{userId}")
	public UserModel getUser(@PathVariable int userId){
		UserModel user = loyalityPointService.getUser(userId);
		if(user == null){
			throw new UserNotFoundException("Not found "  + userId);
		}
		return user;
	}
	
	@GetMapping("/users/{userId}/cards")
	public List<PointCardModel> getPointCards(@PathVariable(required=true) int userId){
		UserModel user = loyalityPointService.getUser(userId);
		if(user == null)
			throw new UserNotFoundException("User not found " + userId);
		return loyalityPointService.getCards(user);
	}
	
	@PostMapping("/users/{userId}/cards")
	public  PointCardModel createCard(@PathVariable int userId){
		UserModel user = loyalityPointService.getUser(userId);
		
		if(user == null)
			throw new UserNotFoundException("User not found " + userId);
		
		return loyalityPointService.addCard(user);		
	}
	
	@PostMapping("/users/{userId}/cards/{cardNumber}")
	public  PointCardModel addPoints2Card(@PathVariable int userId,@PathVariable String cardNumber,@RequestParam int points){
		UserModel user = loyalityPointService.getUser(userId);
		
		if(user == null)
			throw new UserNotFoundException("User not found " + userId);
		
		PointCardModel pcm = loyalityPointService.getCardDetails(cardNumber);
		
		if(pcm == null)
			throw new CardNotFoundException("Card not available");
		
		return loyalityPointService.updatePoints(pcm, points);	
	}
	
	@DeleteMapping("/users/{userId}/cards/{cardNumber}")
	public  void deleteCard(@PathVariable int userId,@PathVariable String cardNumber){
		UserModel user = loyalityPointService.getUser(userId);
		
		if(user == null)
			throw new UserNotFoundException("User not found " + userId);
		PointCardModel card = loyalityPointService.getCardDetails(cardNumber);
		
		if(card == null)
			throw new CardNotFoundException("No card registered with " + cardNumber + " for the user " + user);
		loyalityPointService.cancelCard(card);
	}
	
	@GetMapping("/users/{userId}/cards/{cardNumber}")
	public PointCardModel getPointCards(@PathVariable int userId,@PathVariable String cardNumber){
		
		UserModel user = loyalityPointService.getUser(userId);
		
		if(user == null)
			throw new UserNotFoundException("User not found " + userId);
		
		return loyalityPointService.getCardDetails(cardNumber);
	}
	
}
