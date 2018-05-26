package com.utn.tp5.services;
import com.utn.tp5.models.Country;
import java.util.List;

import com.utn.tp5.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    @Autowired
    CountryRepository countryRepository;


    public List<Country> getAll() {
        return countryRepository.findAll();
    }
}
