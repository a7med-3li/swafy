package com.vamo.rating.repository;

import com.vamo.rating.entity.RideRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRatingRepository extends JpaRepository<RideRating, Long> {
}
