package com.utn.tp5.services;
import java.util.List;

import com.utn.tp5.models.State;
import com.utn.tp5.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    @Autowired
    StateRepository stateRepository;

    public List<State> getAll() {
        return stateRepository.findAll();
    }

    public State getById(long id) {
        return stateRepository.findById(id);
    }

    public State save(State state) {
        return stateRepository.save(state);
    }
}
