package com.utn.tp5.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.utn.tp5.models.Airport;
import com.utn.tp5.models.City;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CityRepository cityRepository;
	
	@Test
	public void testFindByIataIgnoreCase() {
		// Given
		City city = new City();
		city.setIata("MDQ");
		this.entityManager.persistAndFlush(city);

		// When
		City mdq = this.cityRepository.findByIataIgnoreCase("MDQ");

		// Then
		assertThat(city.getIata()).isEqualTo(mdq.getIata());
	}

}
