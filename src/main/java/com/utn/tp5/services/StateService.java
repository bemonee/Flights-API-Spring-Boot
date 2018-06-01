package com.utn.tp5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.State;
import com.utn.tp5.repositories.StateRepository;

@Service
public class StateService extends AGenericCrudeableService<StateRepository, State> {

	@Autowired
	public StateService(StateRepository repo) {
		super(repo);
	}
}
