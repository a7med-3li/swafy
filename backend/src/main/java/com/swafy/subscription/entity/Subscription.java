package com.swafy.subscription.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Subscription {
	
	@Id
	private Long id;
	
	
}
