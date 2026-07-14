package com.kurup.loyalityapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="card")
public class PointCardModel {
	
	@Id
	@GeneratedValue
	private int cardId;
	
	private String cardNumber;
	
	@OneToOne
	@JoinColumn(name="userId",referencedColumnName="userId")
	UserModel user;
	
	private long points;
	
	private int status;
	
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel customer) {
		this.user = customer;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}

