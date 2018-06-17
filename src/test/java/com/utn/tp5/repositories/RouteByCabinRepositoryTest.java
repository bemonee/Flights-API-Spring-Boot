package com.utn.tp5.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Cabin;
import com.utn.tp5.models.City;
import com.utn.tp5.models.Country;
import com.utn.tp5.models.Route;
import com.utn.tp5.models.RouteByCabin;
import com.utn.tp5.models.State;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RouteByCabinRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private RouteByCabinRepository rbcRepository;

	@Test
	public void testFindByCabinIdAndRouteId() {
		// Given
		Cabin cabin = new Cabin();
		cabin.setName("ASD");
		cabin.setRoutesByCabins(null);
		this.entityManager.persistAndFlush(cabin);
		
		Route route = new Route();
		
		Country country = new Country();
		country.setIso2("AR");
		country.setName("Argentina");
		this.entityManager.persistAndFlush(country);
		
		State state = new State();
		state.setIata("BSAS");
		state.setName("Buenos Aires");
		state.setCountry(country);
		this.entityManager.persistAndFlush(state);
		
		City city = new City();
		city.setIata("MDQ");
		city.setName("Mar del Plata");
		city.setState(state);
		this.entityManager.persistAndFlush(city);
		
		Airport origin = new Airport();
		origin.setIata("MDQ");
		origin.setName("Mar del Plata");
		origin.setCity(city);
		this.entityManager.persistAndFlush(origin);
		
		Airport destination = new Airport();
		destination.setIata("AEP");
		destination.setName("Aeroparque");
		destination.setCity(city);
		this.entityManager.persistAndFlush(destination);
		
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);
		this.entityManager.persistAndFlush(route);
		
		RouteByCabin aux = new RouteByCabin();
		aux.setCabin(cabin);
		aux.setRoute(route);
		this.entityManager.persistAndFlush(aux);
		
		// When
		RouteByCabin rbc = this.rbcRepository.findByCabinIdAndRouteId(cabin.getId(), route.getId());
		
		// Then
		assertThat(rbc.getCabin().getId()).isEqualTo(cabin.getId());
		assertThat(rbc.getRoute().getId()).isEqualTo(route.getId());
	}

}
