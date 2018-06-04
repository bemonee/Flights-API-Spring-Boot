package com.utn.tp5.repositories;

import com.utn.tp5.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    public Route findByOriginAirportIdAndDestinationAiportId(Long idOrigin, Long idDestination);
}
