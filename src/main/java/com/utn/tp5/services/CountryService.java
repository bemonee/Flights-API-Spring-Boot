package com.utn.tp5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Country;
import com.utn.tp5.repositories.CountryRepository;

@Service
public class CountryService extends AGenericCrudeableService<CountryRepository, Country> {

	@Autowired
	public CountryService(CountryRepository repo) {
		super(repo);
	}
}