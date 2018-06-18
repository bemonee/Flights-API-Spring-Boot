package com.utn.tp5.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Cabin;
import com.utn.tp5.models.Route;
import com.utn.tp5.models.RouteByCabin;
import com.utn.tp5.repositories.RouteByCabinRepository;

@RunWith(SpringRunner.class)
public class RouteByCabinServiceTest {

	RouteByCabinRepository repo = Mockito.mock(RouteByCabinRepository.class);

	@InjectMocks
	@Spy
	RouteByCabinService rbcService = new RouteByCabinService(repo);

	@Test
	public void testFindByCabinAndRoute() {
		// Given
		Airport origin = new Airport();
		Airport destination = new Airport();
		Route route = new Route();
		route.setId(new Long(1));
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);

		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));
		cabin.setName("Turista");

		RouteByCabin rbc = new RouteByCabin();
		rbc.setCabin(cabin);
		rbc.setRoute(route);

		// When
		Mockito.when(repo.findByCabinIdAndRouteId(cabin.getId(), route.getId())).thenReturn(rbc);
		RouteByCabin found = this.rbcService.findByCabinAndRoute(cabin, route);

		// Then
		Mockito.verify(repo, Mockito.times(1)).findByCabinIdAndRouteId(cabin.getId(), route.getId());
		assertThat(found.getCabin().getId()).isEqualTo(cabin.getId());
		assertThat(found.getRoute().getId()).isEqualTo(route.getId());
	}

	@Test
	public void testFindById() {
		// Given
		RouteByCabin rbc = new RouteByCabin();
		Long id = new Long(1);
		rbc.setId(id);
		Optional<RouteByCabin> optRbc = Optional.of(rbc);

		// When
		Mockito.when(repo.findById(id)).thenReturn(optRbc);
		Optional<RouteByCabin> found = this.rbcService.findById(id);

		// Then
		Mockito.verify(repo, Mockito.times(1)).findById(id);
		assertThat(found.get().getId()).isEqualTo(rbc.getId());
	}

	@Test
	public void testFindAll() {
		// Given
		RouteByCabin rbc = new RouteByCabin();
		Long id = new Long(1);
		rbc.setId(id);
		List<RouteByCabin> list = new ArrayList<>();
		list.add(rbc);

		// When
		Mockito.when(repo.findAll()).thenReturn(list);
		List<RouteByCabin> found = this.rbcService.findAll();

		// Then
		Mockito.verify(repo, Mockito.times(1)).findAll();
		assertThat(found.size()).isEqualTo(list.size());
	}

}
