package com.utn.tp5.repositories;

import com.utn.tp5.models.State;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends IataRepository<State, Long> {
}
