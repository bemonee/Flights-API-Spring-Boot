package com.utn.tp5.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Country;
import com.utn.tp5.repositories.CountryRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;

	public List<Country> getAll() {
		return this.countryRepository.findAll();
	}

	public Country getById(Long id) {
		return this.countryRepository.getOne(id);
	}

	public Country save(Country country) {
		return this.countryRepository.save(country);
	}

	public void delete(Country country) {
		this.countryRepository.delete(country);
	}
}