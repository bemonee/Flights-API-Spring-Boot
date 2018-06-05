package com.utn.tp5.controllers;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utn.tp5.converters.GenericConverter;
import com.utn.tp5.dtos.AirportDTO;
import com.utn.tp5.dtos.CityDTO;
import com.utn.tp5.models.Airport;
import com.utn.tp5.models.City;
import com.utn.tp5.services.AirportService;
import com.utn.tp5.services.CityService;

import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
public class AirportController {

	@Autowired
	AirportService airportService;

	@Autowired
	CityService cityService;

	@Autowired
	GenericConverter<Airport, AirportDTO> converter;

	@GetMapping("/airports")
	public ResponseEntity<?> getAll() {
		List<Airport> airports = this.airportService.findAll();
		if (airports.isEmpty()) {
			return new ResponseEntity<>(airports, HttpStatus.OK); // El recurso existe pero esta vacio, 200
		} else {
			List<AirportDTO> airportsDTOs;
			airportsDTOs = this.converter.modelsToDTOs(airports, AirportDTO.class);
			return new ResponseEntity<>(airportsDTOs, HttpStatus.OK);
		}
	}

	@GetMapping("/airports/{iata}/routes")
	public ResponseEntity<?> getDestinationAirportsByOriginAiport(@PathVariable("iata") String iata) {
		Airport origin = this.airportService.findByIata(iata);
		if (origin == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // El recurso no existe, 404
		}

		Set<Airport> destionationAirports = this.airportService.getDestinationAirportsByOrigin(origin);
		if (destionationAirports.isEmpty()) {
			return new ResponseEntity<>(destionationAirports, HttpStatus.OK); // El recurso existe pero esta vacio, 200
		} else {
			List<AirportDTO> destinationDtos;
			destinationDtos = this.converter.modelsToDTOs(destionationAirports, AirportDTO.class);
			return new ResponseEntity<>(destinationDtos, HttpStatus.OK);
		}
	}

	@PostMapping("/airports")
	public ResponseEntity<?> create(@Valid @RequestBody AirportDTO airportDTO) {
		String iata = airportDTO.getIata();
		if (iata == null || iata.length() < 2 || iata.length() > 3) {
			return new ResponseEntity<>("El codigo iata debe contener entre 2 y 3 caracteres", HttpStatus.BAD_REQUEST);
		}

		iata = iata.toUpperCase();
		Airport checkAirport = this.airportService.findByIata(iata);
		if (checkAirport != null) {
			return new ResponseEntity<>("Ya existe un aeropuerto con ese codigo iata", HttpStatus.CONFLICT);
		}

		String name = airportDTO.getName();
		if (name == null || name.length() < 6 || name.length() > 50) {
			return new ResponseEntity<>("El nombre es debe contener entre 6 y 50 caracteres", HttpStatus.BAD_REQUEST);
		}

		CityDTO cityDTO = airportDTO.getCity();
		String cityIata;
		if (cityDTO == null) {
			return new ResponseEntity<>("La ciudad es obligatoria", HttpStatus.BAD_REQUEST);
		} else {
			cityIata = cityDTO.getIata();
			if (cityIata == null || cityIata.length() < 2 || cityIata.length() > 3) {
				return new ResponseEntity<>("El codigo iata de la ciudad debe contener entre 2 y 3 caracteres",
						HttpStatus.BAD_REQUEST);
			}
		}

		City city = this.cityService.findByIata(cityIata);
		if (city == null) {
			return new ResponseEntity<>("El codigo iata de la ciudad es invalido", HttpStatus.BAD_REQUEST);
		}

		Airport airport = new Airport();
		try {
			airport.setIata(iata);
			airport.setName(name);
			airport.setCity(city);
			airport = this.airportService.save(airport);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(this.converter.modelToDTO(airport, AirportDTO.class), HttpStatus.OK);
	}

	@DeleteMapping("/airports/{iata}")
	public ResponseEntity<?> delete(@PathVariable(value = "iata") String iata) {
		Airport airport = this.airportService.findByIata(iata);
		if (airport == null) {
			return new ResponseEntity<>("No existe un aeropuerto con el codigo iata especificado",
					HttpStatus.BAD_REQUEST);
		} else {
			this.airportService.delete(airport);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

	}

	@PatchMapping("/airports/{iata}")
	public ResponseEntity<?> update(@RequestBody AirportDTO airportDTO, @PathVariable("iata") String iata) {
		
		Airport airport = this.airportService.findByIata(iata);
		boolean applyChanges = false;
		if (airport == null) {
			return new ResponseEntity<>("No existe un aeropuerto con el codigo iata especificado",
					HttpStatus.BAD_REQUEST);
		} else {
			String iataDTO = airportDTO.getIata();
			if (iataDTO != null) {
				if (iataDTO.length() < 2 || iataDTO.length() > 3) {
					return new ResponseEntity<>("El codigo iata debe contener entre 2 y 3 caracteres",
							HttpStatus.BAD_REQUEST);
				} else {
					iataDTO = iataDTO.toUpperCase();
					Airport checkAirport = this.airportService.findByIata(iataDTO);
					if (checkAirport != null) {
						return new ResponseEntity<>("Ya existe un aeropuerto con ese codigo iata", HttpStatus.CONFLICT);
					} else {
						airport.setIata(iataDTO);
						applyChanges = true;
					}
				}
			}

			String nameDTO = airportDTO.getName();
			if (nameDTO != null) {
				if (nameDTO.length() < 6 || nameDTO.length() > 50) {
					return new ResponseEntity<>("El nombre es debe contener entre 6 y 50 caracteres",
							HttpStatus.BAD_REQUEST);
				} else {
					airport.setName(nameDTO);
					applyChanges = true;
				}
			}

			CityDTO cityDTO = airportDTO.getCity();
			if (cityDTO != null) {
				String cityIataDTO = cityDTO.getIata();
				if (cityIataDTO.length() < 2 || cityIataDTO.length() > 3) {
					return new ResponseEntity<>("El codigo iata de la ciudad debe contener entre 2 y 3 caracteres",
							HttpStatus.BAD_REQUEST);
				} else {
					City city = this.cityService.findByIata(cityIataDTO);
					if (city == null) {
						return new ResponseEntity<>("El codigo iata de la ciudad es invalido", HttpStatus.BAD_REQUEST);
					} else {
						airport.setCity(city);
						applyChanges = true;
					}
				}
			}
		}

		if (applyChanges) {
			try {
				airport = this.airportService.save(airport);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(this.converter.modelToDTO(airport, AirportDTO.class), HttpStatus.OK);
		}
		return new ResponseEntity<>("No se encontraron modificaciones a realizar", HttpStatus.BAD_REQUEST);
	}
}
