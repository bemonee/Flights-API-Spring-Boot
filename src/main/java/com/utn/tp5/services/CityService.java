package com.utn.tp5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.City;
import com.utn.tp5.repositories.CityRepository;

@Service
public class CityService extends AGenericIataService<CityRepository, City> { 
	@Autowired
	public CityService(CityRepository repo) {
		super(repo);
	}
}
