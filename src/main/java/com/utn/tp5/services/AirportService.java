package com.utn.tp5.services;

import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AirportService extends AGenericIataService<AirportRepository, Airport> {

	@Autowired
	public AirportService(AirportRepository repo) {
		super(repo);
	}

	public Set<Airport> getDestinationAirportsByOrigin(Airport origin) {
		Set<Airport> destinationAirports = new HashSet<>();

		List<Route> originRoutes = origin.getOriginRoutes();
		if (!originRoutes.isEmpty()) {
			for (Route route : originRoutes) {
				destinationAirports.add(route.getDestinationAiport());
			}
		}

		return destinationAirports;
	}
}
