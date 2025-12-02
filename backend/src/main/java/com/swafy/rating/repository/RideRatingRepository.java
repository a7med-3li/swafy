package com.swafy.rating.repository;

import com.swafy.rating.entity.RideRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRatingRepository extends JpaRepository<RideRating, Long> {
}
