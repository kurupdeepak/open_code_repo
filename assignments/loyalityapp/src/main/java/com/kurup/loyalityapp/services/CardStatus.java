package com.kurup.loyalityapp.services;

public enum CardStatus{
	ACTIVE(1),
	
	CANCELLED(0),
	
	SUSPENDED(1);
	
	int state;
	CardStatus(int s){
		this.state = s;
	}
	
	int getState() {
		return state;
	}
}