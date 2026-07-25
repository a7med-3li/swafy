package com.vamo.corridor.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String name;
	
	@NotBlank
	@Column(nullable = false, columnDefinition = "TEXT")
	private String route;
	
	@NotNull
	private Double price;
	
	@OneToMany(mappedBy = "corridor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VBS> stops = new ArrayList<>();
	
	public void addStop(VBS stop) {
		stops.add(stop);
		stop.setCorridor(this);
	}
}
