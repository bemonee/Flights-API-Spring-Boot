package com.utn.tp5.services;

import com.utn.tp5.models.Airport;
import com.utn.tp5.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AirportService {
    @Autowired
    private AirportRepository cityRepository;

    public List<Airport> getAll() {
        return cityRepository.findAll();
    }

    public Airport getByID(long id) {
        return cityRepository.findById(id);
    }

    public Airport save(Airport airport) {
        return cityRepository.save(airport);
    }
}
