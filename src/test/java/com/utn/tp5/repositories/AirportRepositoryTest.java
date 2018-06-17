package com.utn.tp5.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.utn.tp5.models.Airport;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AirportRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AirportRepository airportRepository;

	@Test
	public void testFindByIataIgnoreCase() {
		// Given
		Airport airport = new Airport();
		airport.setIata("MDQ");
		this.entityManager.persistAndFlush(airport);

		// When
		Airport mdq = this.airportRepository.findByIataIgnoreCase("MDQ");

		// Then
		assertThat(airport.getIata()).isEqualTo(mdq.getIata());
	}

}
