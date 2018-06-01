package com.utn.tp5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utn.tp5.models.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
	public Airport findByIata(String iata);
}
