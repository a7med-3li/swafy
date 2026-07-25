package com.vamo.corridor.repository;

import com.vamo.corridor.entity.Corridor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorridorRepository extends JpaRepository<Corridor, Long> {

}
