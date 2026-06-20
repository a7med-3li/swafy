package com.swafy.corridor.entity;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "corridor")
public class Corridor {
	
	@Id
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String name;
	
	@NotBlank
	@Column(nullable = false)
	private String route;
	
	@NotNull
	private double price;
	
	private List<String> stops;
}
