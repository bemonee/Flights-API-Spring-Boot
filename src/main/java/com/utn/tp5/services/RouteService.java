package com.utn.tp5.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.RouteRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class RouteService {
	@Autowired
	private RouteRepository cityRepository;

	public List<Route> getAll() {
		return cityRepository.findAll();
	}

	public Route getByID(Long id) {
		return cityRepository.getOne(id);
	}

	public Route save(Route route) {
		return cityRepository.save(route);
	}
}
