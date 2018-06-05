package com.utn.tp5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Cabin;
import com.utn.tp5.models.Route;
import com.utn.tp5.models.RouteByCabin;
import com.utn.tp5.repositories.RouteByCabinRepository;

@Service
public class RouteByCabinService extends AGenericCrudeableService<RouteByCabinRepository, RouteByCabin> {
	@Autowired
	public RouteByCabinService(RouteByCabinRepository repo) {
		super(repo);
	}
	
	public RouteByCabin findByCabinAndRoute(Cabin cabin, Route route) {
		return this.repo.findByCabinIdAndRouteId(cabin.getId(), route.getId());
	}
}
