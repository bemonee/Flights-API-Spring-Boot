package com.utn.tp5.controllers;

import java.util.List;

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
import com.utn.tp5.dtos.RouteDTO;
import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Route;
import com.utn.tp5.services.AirportService;
import com.utn.tp5.services.RouteService;

import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
public class RouteController {

	@Autowired
	RouteService routeService;

	@Autowired
	AirportService airportService;

	@Autowired
	GenericConverter<Route, RouteDTO> converter;

	// Me mandan una ruta DTO
	// Va a tener un airportDTO de origen y un airportDTO de destino con sus iatas
	// Hacer las validaciones

	/* Crear ruta */

	@PostMapping("/routes")
	public ResponseEntity<?> create(@Valid @RequestBody RouteDTO routeDTO) {
		AirportDTO airportOriginDTO = routeDTO.getOrigin();
		AirportDTO airportDestinationDTO = routeDTO.getDestination();

		if (airportOriginDTO == null) {
			return new ResponseEntity<>("El aeropuerto de origen no debe ser nulo", HttpStatus.BAD_REQUEST);
		}

		if (airportOriginDTO.getIata() == null) {
			return new ResponseEntity<>("El codigo iata del aeropuerto de origen no debe ser nulo",
					HttpStatus.BAD_REQUEST);
		}

		if (airportDestinationDTO == null) {
			return new ResponseEntity<>("El aeropuerto de destino no debe ser nulo", HttpStatus.BAD_REQUEST);
		}

		if (airportDestinationDTO.getIata() == null) {
			return new ResponseEntity<>("El codigo iata del aeropuerto de destino no debe ser nulo",
					HttpStatus.BAD_REQUEST);
		}

		if (airportOriginDTO.getIata().length() < 2 || airportOriginDTO.getIata().length() > 3) {
			return new ResponseEntity<>("El codigo iata de origen debe contener entre 2 y 3 caracteres",
					HttpStatus.BAD_REQUEST);
		}

		if (airportDestinationDTO.getIata().length() < 2 || airportDestinationDTO.getIata().length() > 3) {
			return new ResponseEntity<>("El codigo iata de destino debe contener entre 2 y 3 caracteres",
					HttpStatus.BAD_REQUEST);
		}

		String iataOrigin = airportOriginDTO.getIata();
		iataOrigin = iataOrigin.toUpperCase();
		Airport airportOrigin = this.airportService.findByIata(iataOrigin);

		if (airportOrigin == null) {
			return new ResponseEntity<>("Ese aeropuerto de origen no existe", HttpStatus.BAD_REQUEST);
		}

		String iataDestination = airportDestinationDTO.getIata();
		iataDestination = iataDestination.toUpperCase();
		Airport airportDestination = this.airportService.findByIata(iataDestination);

		if (airportDestination == null) {
			return new ResponseEntity<>("Ese aeropuerto de destino no existe", HttpStatus.BAD_REQUEST);
		}

		Route route = routeService.findByOriginAndDestination(airportOrigin, airportDestination);
		if (route != null) {
			return new ResponseEntity<>("Esa ruta ya existe", HttpStatus.CONFLICT);
		}

		Route newRoute = new Route();
		newRoute.setOriginAirport(airportOrigin);
		newRoute.setDestinationAiport(airportDestination);
		newRoute = this.routeService.save(newRoute);
		return new ResponseEntity<>(this.converter.modelToDTO(newRoute, RouteDTO.class), HttpStatus.OK);
	}
	/* Listar rutas */

	@GetMapping("routes")
	public ResponseEntity<?> getAllAirports() {
		List<Route> routes = this.routeService.findAll();
		if (routes.isEmpty()) {
			return new ResponseEntity<>(routes, HttpStatus.OK); // El recurso existe pero esta vacio, 200
		} else {
			List<RouteDTO> routesDTOs;
			routesDTOs = this.converter.modelsToDTOs(routes, RouteDTO.class);
			return new ResponseEntity<>(routesDTOs, HttpStatus.OK);
		}
	}

	@GetMapping("/routes/origin/{iataOrigin}/destination/{iataDestination}")
	public ResponseEntity<?> getRouteByOriginAndDestinationAirport(@PathVariable("iataOrigin") String iataOrigin,
			@PathVariable("iataDestination") String iataDestination) {
		Airport airportOrigin = this.airportService.findByIata(iataOrigin);
		Airport airportDestination = this.airportService.findByIata(iataDestination);
		if (airportOrigin == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // El recurso no existe, 404
		}
		if (airportDestination == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		Route route = this.routeService.findByOriginAndDestination(airportOrigin, airportDestination);

		if (route == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // El recurso no existe
		} else {
			return new ResponseEntity<>(this.converter.modelToDTO(route, RouteDTO.class), HttpStatus.OK);
		}
	}

	/* Eliminar ruta */

	@DeleteMapping("/route/origin/{iataOrigin}/destination/{iataDestination}")
	public ResponseEntity<?> delete(@PathVariable("iataOrigin") String iataOrigin,
			@PathVariable("iataDestination") String iataDestination) {
		Airport airportOrigin = this.airportService.findByIata(iataOrigin);
		Airport airportDestination = this.airportService.findByIata(iataDestination);

		if (airportOrigin == null) {
			return new ResponseEntity<>("No existe un aeropuerto de origen con el codigo iata especificado",
					HttpStatus.BAD_REQUEST);
		}

		if (airportDestination == null) {
			return new ResponseEntity<>("No existe un aeropuerto de destino con el codigo iata especificado",
					HttpStatus.BAD_REQUEST);
		}

		Route route = this.routeService.findByOriginAndDestination(airportOrigin, airportDestination);

		if (route == null) {
			return new ResponseEntity<>("No existe la ruta con esa iata de origen y esa de destino",
					HttpStatus.BAD_REQUEST);
		}

		this.routeService.delete(route);

		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

	}

	/* Modificar ruta */

	@PatchMapping("/routes/origin/{iataOrigin}/destination/{iataDestination}")
	public ResponseEntity<?> update(@PathVariable("iataOrigin") String iataOrigin,
			@PathVariable("iataDestination") String iataDestination, @Valid @RequestBody RouteDTO routeDTO) {
		Airport airportOrigin = this.airportService.findByIata(iataOrigin);
		if (airportOrigin == null) {
			return new ResponseEntity<>("No existe un aeropuerto con el codigo iata especificado",
					HttpStatus.BAD_REQUEST);
		}
		Airport airportDestination = this.airportService.findByIata(iataDestination);
		if (airportDestination == null) {
			return new ResponseEntity<>("No existe un aeropuerto con el codigo iata especificado",
					HttpStatus.BAD_REQUEST);
		}

		Route route = this.routeService.findByOriginAndDestination(airportOrigin, airportDestination);

		boolean applyChanges = false;

		if (route == null) {
			return new ResponseEntity<>("No existe un ruta con esa iata de origen y esa iata de destino",
					HttpStatus.BAD_REQUEST);
		} else {

			String iataOriginDTO = routeDTO.getOrigin().getIata();
			String iataDestinationDTO = routeDTO.getDestination().getIata();

			if (iataOriginDTO != null && iataDestinationDTO != null) {
				if (iataOriginDTO.length() < 2 || iataOriginDTO.length() > 3 && iataDestinationDTO.length() < 2
						|| iataDestinationDTO.length() > 3) {
					return new ResponseEntity<>("El codigo iata debe contener entre 2 y 3 caracteres",
							HttpStatus.BAD_REQUEST);
				} else {
					Route checkRoute = this.routeService.findByOriginAndDestination(airportOrigin, airportDestination);
					if (checkRoute != null) {
						return new ResponseEntity<>("Ya existe una ruta con esos codigos iata", HttpStatus.CONFLICT);
					} else {
						route.setOriginAirport(airportOrigin);
						route.setDestinationAiport(airportDestination);
						applyChanges = true;
					}
				}
			}

			if (applyChanges) {
				route = this.routeService.save(route);
				return new ResponseEntity<>(this.converter.modelToDTO(route, RouteDTO.class), HttpStatus.OK);
			}
			return new ResponseEntity<>("No se encontraron modificaciones a realizar", HttpStatus.BAD_REQUEST);
		}

	}
}
