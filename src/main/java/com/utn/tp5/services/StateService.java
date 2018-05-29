package com.utn.tp5.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.State;
import com.utn.tp5.repositories.StateRepository;

@Service
public class StateService {

	@Autowired
	StateRepository stateRepository;

	public List<State> getAll() {
		return stateRepository.findAll();
	}

	public State getById(Integer id) {
		return stateRepository.getOne(id);
	}

	public State save(State state) {
		return stateRepository.save(state);
	}
}
