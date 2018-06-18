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
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.RouteRepository;

@RunWith(SpringRunner.class)
public class RouteServiceTest {

	RouteRepository repo = Mockito.mock(RouteRepository.class);

	@InjectMocks
	@Spy
	RouteService routeService = new RouteService(repo);

	@Test
	public void testFindByOriginAndDestination() {
		// Given
		Airport origin = new Airport();
		origin.setId(new Long(1));
		Airport destination = new Airport();
		destination.setId(new Long(2));

		Route route = new Route();
		route.setId(new Long(1));
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);

		// When
		Mockito.when(repo.findByOriginAirportIdAndDestinationAiportId(origin.getId(), destination.getId())).thenReturn(route);
		Route found = this.routeService.findByOriginAndDestination(origin, destination);

		// Then
		assertThat(found.getId()).isEqualTo(route.getId());
	}

}
