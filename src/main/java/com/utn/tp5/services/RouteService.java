package com.utn.tp5.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.RouteRepository;

public class RouteService {
	@Autowired
	private RouteRepository cityRepository;

	public List<Route> getAll() {
		return cityRepository.findAll();
	}

	public Route getByID(Integer id) {
		return cityRepository.getOne(id);
	}

	public Route save(Route route) {
		return cityRepository.save(route);
	}
}
