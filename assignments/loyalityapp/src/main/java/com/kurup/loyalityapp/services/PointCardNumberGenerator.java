package com.kurup.loyalityapp.services;

import org.springframework.stereotype.Component;

@Component
public class PointCardNumberGenerator {
	private static final String groupA = "4352";
	private static final String groupB = "6789";
	private static final String groupC = "4562";
	public static final int MIN_CARD_NUMBER_LENGTH = 8;
	
	private static int sequence = 1000;
	
	private int customerNumber;
	
	String generateNumber() {
		this.customerNumber = sequence ++;
		return new StringBuilder(groupA).append(groupB).append(groupC).append(customerNumber).toString();
	}
}
