package com.utn.tp5.repositories;

import org.springframework.stereotype.Repository;

import com.utn.tp5.models.City;

@Repository
public interface CityRepository extends IataRepository<City, Long> {

}
