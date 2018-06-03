package com.utn.tp5.repositories;

import com.utn.tp5.models.City;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends IataRepository<City, Long> {

}
