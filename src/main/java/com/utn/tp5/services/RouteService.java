package com.utn.tp5.services;

import com.utn.tp5.models.Route;
import com.utn.tp5.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RouteService {
    @Autowired
    private RouteRepository cityRepository;

    public List<Route> getAll() {
        return cityRepository.findAll();
    }

    public Route getByID(long id) {
        return cityRepository.findById(id);
    }

    public Route save(Route route) {
        return cityRepository.save(route);
    }
}
