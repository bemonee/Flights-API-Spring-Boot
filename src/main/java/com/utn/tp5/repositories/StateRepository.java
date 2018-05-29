package com.utn.tp5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utn.tp5.models.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
}
