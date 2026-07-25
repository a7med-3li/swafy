package com.vamo.corridor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vbs")
public class VBS {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "corridor_id", referencedColumnName = "id", nullable = false)
	private Corridor corridor;
	
	@NotBlank
	@Column(nullable = false, unique = true)
	private String name;
	
	@NotNull
	@Column(nullable = false)
	private Double latitude;
	
	@NotNull
	@Column(nullable = false)
	private Double longitude;
	
}
