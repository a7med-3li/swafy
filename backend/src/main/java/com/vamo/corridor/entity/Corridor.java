package com.vamo.corridor.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.vamo.common.entity.Location;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
	private String title;
	
	@NotBlank
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "latitude", column = @Column(name = "start_lat")),
			@AttributeOverride(name = "longitude", column = @Column(name = "start_lng")),
			@AttributeOverride(name = "addressName", column = @Column(name = "start_address"))
	})
	private Location start;
	
	@NotBlank
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "latitude", column = @Column(name = "destination_lat")),
			@AttributeOverride(name = "longitude", column = @Column(name = "destination_lng")),
			@AttributeOverride(name = "addressName", column = @Column(name = "destination_address"))
	})
	private Location destination;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;
	
	@OneToMany(mappedBy = "corridor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VBS> stops = new ArrayList<>();
	
	public void addStop(VBS stop) {
		stops.add(stop);
		stop.setCorridor(this);
	}
}
