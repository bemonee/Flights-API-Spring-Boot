package com.utn.tp5.services;

import com.utn.tp5.models.City;
import com.utn.tp5.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService extends AGenericIataService<CityRepository, City> { 
	@Autowired
	public CityService(CityRepository repo) {
		super(repo);
	}
}
