package com.utn.tp5.repositories;

import com.utn.tp5.models.Airport;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends IataRepository<Airport, Long> {
}
