package com.utn.tp5.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.AirportRepository;

@RunWith(SpringRunner.class)
public class AirportServiceTest {

	AirportRepository repo = Mockito.mock(AirportRepository.class);

	@InjectMocks
	@Spy
	AirportService airportService = new AirportService(repo);

	@Test
	public void testGetDestinationAirportsByOrigin() {
		// Given
		Airport origin = new Airport();
		Airport destination = new Airport();

		Route route = new Route();
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);

		List<Route> list = new ArrayList<>();
		list.add(route);
		origin.setOriginRoutes(list);

		// When
		Set<Airport> found = this.airportService.getDestinationAirportsByOrigin(origin);

		// Then
		assertThat(found.contains(destination)).isEqualTo(true);
	}

}
