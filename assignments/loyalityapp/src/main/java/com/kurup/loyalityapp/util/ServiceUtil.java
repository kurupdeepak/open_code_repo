package com.kurup.loyalityapp.util;

import com.google.common.base.Preconditions;
import com.kurup.loyalityapp.services.PointCardNumberGenerator;

public class ServiceUtil {
	public static void validateParameterNotNull(Object parameter, String nullMessage) {
		Preconditions.checkArgument(parameter != null, nullMessage);
	}

	public static void isValidCardNumber(String cardNumber,String invalidNumberMessage) {
		validateParameterNotNull(cardNumber, "Card Number is null");
		Preconditions.checkArgument(isValidCard(cardNumber), invalidNumberMessage);		
	}
	
	private static boolean isValidCard(String cardNumber) {
		return (cardNumber.length() < PointCardNumberGenerator.MIN_CARD_NUMBER_LENGTH) || 	cardNumber.matches("^[0-9]+$");
	}
	
	public static void main(String a[]) {
		String card = "43526789456211AB";
	}
}