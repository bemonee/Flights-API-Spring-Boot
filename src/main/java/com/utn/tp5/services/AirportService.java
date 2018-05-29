package com.utn.tp5.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Airport;
import com.utn.tp5.repositories.AirportRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AirportService {
	@Autowired
	private AirportRepository cityRepository;

	public List<Airport> getAll() {
		return cityRepository.findAll();
	}

	public Airport getByID(Long id) {
		return cityRepository.getOne(id);
	}

	public Airport save(Airport airport) {
		return cityRepository.save(airport);
	}
}
