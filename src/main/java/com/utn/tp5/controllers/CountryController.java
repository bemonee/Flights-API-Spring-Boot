package com.utn.tp5.controllers;

import com.utn.tp5.services.CountryService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
public class CountryController {

	@Autowired
	CountryService countryService;
	/*
	// Get All Countries
	@GetMapping("/countries")
	public List<Country> getAllCountries() {
		List<Country> countries = this.countryService.findAll();
		return countries;
	}

	// Get a Single Country
	@GetMapping("/countries/{id}")
	public Country getCountryById(@PathVariable(value = "id") Long id) {
		Optional<Country> country;
		country = countryService.findById(id);
		return country.get();
		 .orElseThrow(() -> new ResourceNotFoundException("Country", "id", Id)); 
	}

	// Create a Country
	@PostMapping("/countries")
	public ResponseEntity<?> createCountry(@Valid @RequestBody CountryDTO countryDTO) {
		return null;
	}

	// Update a Country
	@PutMapping("/countries/{id}")
	public Country updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Country countryDetails) {

		Country country = countryService.findById(noteId);
		 .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId)); 

		country.setName(countryDetails.getName());
		country.setIso2(countryDetails.getIso2());

		Country updatedCountry = countryService.save(country);
		return updatedCountry;
	}*/

}
