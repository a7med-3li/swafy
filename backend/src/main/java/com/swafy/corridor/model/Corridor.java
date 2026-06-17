package com.swafy.corridor.model;

import java.util.List;
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
	
	private String name;
	
	private String route;
	
	private double price;
	
	private List<String> stops;
}
