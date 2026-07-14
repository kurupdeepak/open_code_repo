package com.kurup.loyalityapp.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class UserModel {
	@Id
	@GeneratedValue
	private int userId;
	
	private String name;
	
	@OneToMany(mappedBy="cardId",targetEntity=PointCardModel.class, cascade=CascadeType.ALL)
	Set<PointCardModel> cards;
	
	public UserModel() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PointCardModel> getCards() {
		return cards;
	}

	public void setCards(Set<PointCardModel> cards) {
		this.cards = cards;
	}
	
}
