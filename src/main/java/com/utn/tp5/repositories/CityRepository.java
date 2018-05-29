package com.utn.tp5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utn.tp5.models.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

}
