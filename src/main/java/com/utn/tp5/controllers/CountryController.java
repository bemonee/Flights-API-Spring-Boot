package com.utn.tp5.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.utn.tp5.models.Country;
import com.utn.tp5.services.CountryService;

@RestController
@RequestMapping("/api")
public class CountryController {

	@Autowired
	CountryService countryService;

	// Get All Countries
	@GetMapping("/countries")
	public List<Country> getAllCountries() {
		List<Country> countries = this.countryService.getAll();
		return countries;
	}

	// Get a Single Country
	@GetMapping("/countries/{id}")
	public Country getCountryById(@PathVariable(value = "id") Long id) {
		return countryService.getById(id);
		/* .orElseThrow(() -> new ResourceNotFoundException("Country", "id", Id)); */
	}

	// Create a Country
	@PostMapping("/countries")
	public Country createCountry(@Valid @RequestBody Country country) {
		return countryService.save(country);
	}

	@RequestMapping(value = "/country", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Country addProduct(@RequestBody Country country) {

		return countryService.save(country);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody Country user, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " + user.getName());

		/*
		 * if (countryService.getById(user)) { System.out.println("A User with name " +
		 * user.getName() + " already exist"); return new
		 * ResponseEntity<Void>(HttpStatus.CONFLICT); }
		 */

		countryService.save(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	// Update a Country
	@PutMapping("/countries/{id}")
	public Country updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Country countryDetails) {

		Country country = countryService.getById(noteId);
		/* .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId)); */

		country.setName(countryDetails.getName());
		country.setIso2(countryDetails.getIso2());

		Country updatedCountry = countryService.save(country);
		return updatedCountry;
	}

	// Delete a Country
	@DeleteMapping("/notes/{id}")
	public ResponseEntity<?> deleteCountry(@PathVariable(value = "id") Long Id) {
		Country country = countryService.getById(Id);
		/* .orElseThrow(() -> new ResourceNotFoundException("Note", "id", Id)); */

		countryService.delete(country);

		return ResponseEntity.ok().build();
	}

	public CountryController() {

	}
}
