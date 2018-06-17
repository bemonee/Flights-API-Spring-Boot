package com.utn.tp5.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.utn.tp5.models.State;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StateRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private StateRepository stateRepository;

	@Test
	public void testFindByIataIgnoreCase() {
		// Given
		State state = new State();
		state.setIata("MDQ");
		this.entityManager.persistAndFlush(state);

		// When
		State mdq = this.stateRepository.findByIataIgnoreCase("MDQ");

		// Then
		assertThat(state.getIata()).isEqualTo(mdq.getIata());
	}

}
