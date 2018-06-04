package com.utn.tp5.services;

import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService extends AGenericCrudeableService<RouteRepository, Route> {

	@Autowired
	RouteRepository routeRepository;

	@Autowired
	public RouteService(RouteRepository repo) {
		super(repo);
	}

	public Route findByOriginAndDestination(Airport originAirport, Airport destinationAirport) {
		Long originId = originAirport.getId();
		Long destinationId = destinationAirport.getId();
		return routeRepository.findByOriginAirportIdAndDestinationAiportId(originId, destinationId);
	}

}
