package com.utn.tp5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utn.tp5.models.RouteByCabin;

public interface RouteByCabinRepository extends JpaRepository<RouteByCabin, Long> {
	public RouteByCabin findByCabinIdAndRouteId(Long idCabin, Long idRoute);
}
