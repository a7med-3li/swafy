package com.swafy.corridor.repository;

import com.swafy.corridor.entity.Corridor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorridorRepo extends JpaRepository<Corridor, Long> {

}
