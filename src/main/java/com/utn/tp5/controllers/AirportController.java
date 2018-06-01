package com.utn.tp5.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utn.tp5.dtos.AirportDTO;
import com.utn.tp5.dtos.CityDTO;
import com.utn.tp5.dtos.CountryDTO;
import com.utn.tp5.dtos.StateDTO;
import com.utn.tp5.models.Airport;
import com.utn.tp5.models.City;
import com.utn.tp5.models.Country;
import com.utn.tp5.models.State;
import com.utn.tp5.services.AirportService;

import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
public class AirportController {

	@Autowired
	AirportService airportService;
	

	// Get All Countries
	@GetMapping("/airports")
	public ResponseEntity<?> getAllAirports() {
		List<Airport> airports = this.airportService.findAll();
		if(airports.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		
		List<AirportDTO> airportsDTOs = new ArrayList<>();
		for(Airport airport : airports) {
			airportsDTOs.add(this.convertToDTO(airport));
		}
		return new ResponseEntity<>(airportsDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/airports/{id}")
	public ResponseEntity<?> getAirport(@PathVariable(value = "id") Long id) {
		Airport airport = this.airportService.getOne(id);
		if(airport == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(this.convertToDTO(airport), HttpStatus.OK);
		
	}
	
	private AirportDTO convertToDTO(Airport airport) {
		City city = airport.getCity();
		State state = city.getState();
		Country country = state.getCountry();
		
		CountryDTO countryDTO = new CountryDTO(country.getName(), country.getIso2());
		StateDTO stateDTO = new StateDTO(state.getIata(), state.getName(), countryDTO);
		CityDTO cityDTO = new CityDTO(city.getIata(), city.getName(), stateDTO);
		AirportDTO airportDTO = new AirportDTO(airport.getIata(), airport.getName(), cityDTO);
		return airportDTO;
	}
}
