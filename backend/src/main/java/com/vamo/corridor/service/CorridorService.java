package com.vamo.corridor.service;

import java.util.List;
import com.vamo.corridor.dto.CorridorStopRequest;
import com.vamo.corridor.dto.SaveCorridorRequest;
import com.vamo.corridor.entity.Corridor;
import com.vamo.corridor.entity.VBS;
import com.vamo.corridor.repository.CorridorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CorridorService {
	
	private final CorridorRepository corridorRepository;
	
	public List<Corridor> getAllCorridors() {
		return corridorRepository.findAll();
	}

	@Transactional
	public Corridor createCorridor(SaveCorridorRequest request) {
		Corridor corridor = new Corridor();
		corridor.setName(request.name());
		corridor.setRoute(request.route());
		corridor.setPrice(request.price());
		
		for (CorridorStopRequest stopDto : request.stops()) {
			VBS stop = new VBS();
			stop.setName(stopDto.name());
			stop.setLatitude(stopDto.latitude());
			stop.setLongitude(stopDto.longitude());
			
			corridor.addStop(stop);
		}
		
		return corridorRepository.save(corridor);
	}
}
