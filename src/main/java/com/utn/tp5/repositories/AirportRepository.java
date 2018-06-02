package com.utn.tp5.repositories;

import org.springframework.stereotype.Repository;

import com.utn.tp5.models.Airport;

@Repository
public interface AirportRepository extends IataRepository<Airport, Long> {
}
