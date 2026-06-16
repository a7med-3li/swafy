package com.swafy.corridor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Corridor {
	
	@Id
	private Long id;
	
	
}
