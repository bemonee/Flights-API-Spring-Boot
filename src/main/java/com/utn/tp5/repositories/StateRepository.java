package com.utn.tp5.repositories;

import org.springframework.stereotype.Repository;

import com.utn.tp5.models.State;

@Repository
public interface StateRepository extends IataRepository<State, Long> {
}
