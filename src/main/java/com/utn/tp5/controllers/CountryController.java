package com.utn.tp5.controllers;
import com.utn.tp5.models.Country;
import java.util.List;
import com.utn.tp5.exception.ResourceNotFoundException;

import com.utn.tp5.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class CountryController {

    @Autowired
    CountryService countryService;

    // Get All Countries
    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        return countryService.getAll();
    }

    // Get a Single Country
    @GetMapping("/countries/{id}")
    public Country getCountryById(@PathVariable(value = "id") Long Id) {
        return countryService.getById(Id);
                /*.orElseThrow(() -> new ResourceNotFoundException("Country", "id", Id));*/
    }

    // Create a Country
    @PostMapping("/countries")
    public Country createCountry(@Valid @RequestBody Country country) {
        return countryService.save(country);
    }
    // Update a Country
    @PutMapping("/countries/{id}")
    public Country updateNote(@PathVariable(value = "id") Long noteId,
                           @Valid @RequestBody Country countryDetails) {

        Country country = countryService.getById(noteId);
                /*.orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));*/

        country.setName(countryDetails.getName());
        country.setIso2(countryDetails.getIso2());

        Country updatedCountry = countryService.save(country);
        return updatedCountry;
    }

    // Delete a Country
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable(value = "id") Long Id) {
        Country country = countryService.getById(Id);
                /*.orElseThrow(() -> new ResourceNotFoundException("Note", "id", Id));*/

        countryService.delete(country);

        return ResponseEntity.ok().build();
    }

    public CountryController() {

    }
}
