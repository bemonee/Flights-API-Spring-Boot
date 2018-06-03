package com.utn.tp5.services;

import com.utn.tp5.models.State;
import com.utn.tp5.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService extends AGenericIataService<StateRepository, State> {

	@Autowired
	public StateService(StateRepository repo) {
		super(repo);
	}
}
