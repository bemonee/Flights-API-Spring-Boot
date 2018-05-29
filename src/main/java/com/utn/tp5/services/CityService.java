package com.utn.tp5.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.City;
import com.utn.tp5.repositories.CityRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class CityService {
	@Autowired
	private CityRepository cityRepository;

	public List<City> getAll() {
		return cityRepository.findAll();
	}

	public City getByID(Long id) {
		return cityRepository.getOne(id);
	}

	public City save(City city) {
		return cityRepository.save(city);
	}
}
