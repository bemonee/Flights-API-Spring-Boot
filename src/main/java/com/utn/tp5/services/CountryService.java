package com.utn.tp5.services;
import com.utn.tp5.models.Country;
import java.util.List;

import com.utn.tp5.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService{

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    public Country getById(long id) {
        return countryRepository.findById(id);
    }

    public Country save(Country country) {
        return countryRepository.save(country);
    }

    public void delete(Country country) {
        countryRepository.delete(country);
    }
}