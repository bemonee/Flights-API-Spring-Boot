package com.utn.tp5.services;

import com.utn.tp5.models.City;
import com.utn.tp5.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public City getByID(long id) {
        return cityRepository.findById(id);
    }

    public City save(City city) {
        return cityRepository.save(city);
    }
}
