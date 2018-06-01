package com.utn.tp5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Airport;
import com.utn.tp5.repositories.AirportRepository;

@Service
public class AirportService extends AGenericCrudeableService<AirportRepository, Airport> {

	@Autowired
	public AirportService(AirportRepository repo) {
		super(repo);
	}

}
