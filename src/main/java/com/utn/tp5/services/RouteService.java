package com.utn.tp5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.RouteRepository;

@Service
public class RouteService extends AGenericCrudeableService<RouteRepository, Route> {
	@Autowired
	public RouteService(RouteRepository repo) {
		super(repo);
	}
}
