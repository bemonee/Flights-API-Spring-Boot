package com.utn.tp5.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.utn.tp5.models.Airport;
import com.utn.tp5.repositories.AirportRepository;

public class AirportService {
	@Autowired
	private AirportRepository cityRepository;

	public List<Airport> getAll() {
		return cityRepository.findAll();
	}

	public Airport getByID(Integer id) {
		return cityRepository.getOne(id);
	}

	public Airport save(Airport airport) {
		return cityRepository.save(airport);
	}
}
