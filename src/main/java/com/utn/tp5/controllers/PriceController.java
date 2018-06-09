package com.utn.tp5.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utn.tp5.converters.GenericConverter;
import com.utn.tp5.dtos.AirportDTO;
import com.utn.tp5.dtos.CabinDTO;
import com.utn.tp5.dtos.PriceDTO;
import com.utn.tp5.dtos.RouteDTO;
import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Cabin;
import com.utn.tp5.models.Price;
import com.utn.tp5.models.Route;
import com.utn.tp5.models.RouteByCabin;
import com.utn.tp5.services.AirportService;
import com.utn.tp5.services.CabinService;
import com.utn.tp5.services.PriceService;
import com.utn.tp5.services.RouteByCabinService;
import com.utn.tp5.services.RouteService;

import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
public class PriceController {

	@Autowired
	private PriceService priceService;

	@Autowired
	private AirportService airportService;

	@Autowired
	private CabinService cabinService;

	@Autowired
	private RouteService routeService;

	@Autowired
	private RouteByCabinService routeByCabinService;

	@Autowired
	private GenericConverter<Price, PriceDTO> converter;

	@GetMapping("/prices")
	public ResponseEntity<?> getAll() {
		List<Price> prices = this.priceService.findAll();
		if (prices.isEmpty()) {
			return new ResponseEntity<>(prices, HttpStatus.OK);
		} else {
			List<PriceDTO> pricesDTOs;
			pricesDTOs = this.converter.modelsToDTOs(prices, PriceDTO.class);
			return new ResponseEntity<>(pricesDTOs, HttpStatus.OK);
		}
	}

	@PostMapping("/prices")
	public ResponseEntity<?> create(@Valid @RequestBody PriceDTO priceDTO) {
		// DTO Validations
		RouteDTO routeDTO = priceDTO.getRoute();
		AirportDTO originDTO;
		String originIataDTO;
		AirportDTO destinationDTO;
		String destinationIataDTO;
		CabinDTO cabinDTO;
		Long cabinIdDTO;
		LocalDate fromDate;
		LocalDate toDate;

		if (routeDTO == null) {
			return new ResponseEntity<>("Es necesario especificar una ruta", HttpStatus.BAD_REQUEST);
		} else {
			originDTO = routeDTO.getOrigin();
			if (originDTO == null) {
				return new ResponseEntity<>("El aeropuerto origen de la ruta es obligatorio", HttpStatus.BAD_REQUEST);
			} else {
				originIataDTO = originDTO.getIata();
				if (originIataDTO == null || originIataDTO.length() < 2 || originIataDTO.length() > 3) {
					return new ResponseEntity<>(
							"El codigo iata del aeropuerto origen debe contener entre 2 y 3 caracteres",
							HttpStatus.BAD_REQUEST);
				}
			}

			destinationDTO = routeDTO.getDestination();
			if (destinationDTO == null) {
				return new ResponseEntity<>("El aeropuerto destino de la ruta es obligatorio", HttpStatus.BAD_REQUEST);
			} else {
				destinationIataDTO = destinationDTO.getIata();
				if (destinationIataDTO == null || destinationIataDTO.length() < 2 || destinationIataDTO.length() > 3) {
					return new ResponseEntity<>(
							"El codigo iata del aeropuerto destino debe contener entre 2 y 3 caracteres",
							HttpStatus.BAD_REQUEST);
				}
			}
		}

		cabinDTO = priceDTO.getCabin();
		if (cabinDTO == null) {
			return new ResponseEntity<>("La cabina es obligatoria para procesar la solicitud", HttpStatus.BAD_REQUEST);
		} else {
			cabinIdDTO = cabinDTO.getId();
			if (cabinIdDTO == null) {
				return new ResponseEntity<>("El ID de la cabina es obligatorio para procesar la solicitud",
						HttpStatus.BAD_REQUEST);
			}
		}

		if (priceDTO.getPrice() <= 0) { // Nothing is free
			return new ResponseEntity<>("El precio es obligatorio para procesar la solicitud", HttpStatus.BAD_REQUEST);
		}

		fromDate = priceDTO.getFromDate();
		if (fromDate == null) {
			return new ResponseEntity<>("La fecha de inicio es obligatoria para procesar la solicitud",
					HttpStatus.BAD_REQUEST);
		}

		toDate = priceDTO.getToDate();
		if (toDate == null) {
			return new ResponseEntity<>("La fecha de fin es obligatoria para procesar la solicitud",
					HttpStatus.BAD_REQUEST);
		}

		if (fromDate.isAfter(toDate)) {
			return new ResponseEntity<>("La fecha de inicio no puede ser menor a la fecha de fin",
					HttpStatus.BAD_REQUEST);
		}
		// End DTO Validations

		// Models Validations
		Airport originAirport = this.airportService.findByIata(originIataDTO);
		if (originAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto origen es incorrecto", HttpStatus.BAD_REQUEST);
		}

		Airport destinationAirport = this.airportService.findByIata(destinationIataDTO);
		if (destinationAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto destino es incorrecto", HttpStatus.BAD_REQUEST);
		}

		Optional<Cabin> cabin = this.cabinService.findById(cabinIdDTO);
		if (!cabin.isPresent()) {
			return new ResponseEntity<>("El ID de la cabina es incorrecta.", HttpStatus.BAD_REQUEST);
		}

		Route route = this.routeService.findByOriginAndDestination(originAirport, destinationAirport);
		if (route == null) {
			return new ResponseEntity<>("La ruta ingresada es inexistente", HttpStatus.BAD_REQUEST);
		}
		// End Models Validations

		Price price = new Price();
		price.setPrice(priceDTO.getPrice());
		price.setFromDate(fromDate);
		price.setToDate(toDate);
		try {
			price = this.priceService.save(price, route, cabin.get());
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@DeleteMapping("/prices/{originIata}/{destinationIata}/{cabinId}")
	public ResponseEntity<?> delete(@PathVariable("originIata") String originIata,
			@PathVariable("destinationIata") String destinationIata, @PathVariable("cabinId") Long cabinId,
			@RequestParam(value = "date", required = false) @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date) {
		// Models Validations
		Airport originAirport = this.airportService.findByIata(originIata);
		if (originAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto origen es incorrecto", HttpStatus.BAD_REQUEST);
		}

		Airport destinationAirport = this.airportService.findByIata(destinationIata);
		if (destinationAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto destino es incorrecto", HttpStatus.BAD_REQUEST);
		}

		Optional<Cabin> cabin = this.cabinService.findById(cabinId);
		if (!cabin.isPresent()) {
			return new ResponseEntity<>("El ID de la cabina es incorrecta.", HttpStatus.BAD_REQUEST);
		}

		Route route = this.routeService.findByOriginAndDestination(originAirport, destinationAirport);
		if (route == null) {
			return new ResponseEntity<>("La ruta ingresada es inexistente", HttpStatus.BAD_REQUEST);
		}
		// End Models Validations

		try {
			if (date.isPresent()) {
				this.priceService.delete(route, cabin.get(), date.get());
			} else {
				this.priceService.delete(route, cabin.get());
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@PatchMapping("/prices/{originIata}/{destinationIata}/{cabinId}/{date}")
	public ResponseEntity<?> update(@PathVariable("originIata") String originIata,
			@PathVariable("destinationIata") String destinationIata, @PathVariable("cabinId") Long cabinId,
			@PathVariable("date") @DateTimeFormat(iso = ISO.DATE) LocalDate date, @RequestBody PriceDTO priceDTO) {
		// Models Validations
		Airport originAirport = this.airportService.findByIata(originIata);
		if (originAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto origen es incorrecto", HttpStatus.BAD_REQUEST);
		}

		Airport destinationAirport = this.airportService.findByIata(destinationIata);
		if (destinationAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto destino es incorrecto", HttpStatus.BAD_REQUEST);
		}

		Optional<Cabin> cabin = this.cabinService.findById(cabinId);
		if (!cabin.isPresent()) {
			return new ResponseEntity<>("El ID de la cabina es incorrecta.", HttpStatus.BAD_REQUEST);
		}

		Route route = this.routeService.findByOriginAndDestination(originAirport, destinationAirport);
		if (route == null) {
			return new ResponseEntity<>("La ruta ingresada es inexistente", HttpStatus.BAD_REQUEST);
		}

		Price price = this.priceService.findPriceByCabinAndRouteAndDate(route, cabin.get(), date);
		if (price == null) {
			return new ResponseEntity<>("No se ha encontrado un precio para la ruta, la cabina y la fecha especificada",
					HttpStatus.BAD_REQUEST);
		}
		// End Models Validations

		boolean applyChanges = false;
		// Validaciones super larga, aburrida y descriptiva para que el que la consuma
		// no haga preguntontas que por cuestion de performance no puedo abstraer
		// (necesito que a la primera de cambio corte ejecucion)
		RouteDTO routeDTO = priceDTO.getRoute();
		RouteByCabin oldRbc = price.getRouteByCabin();
		if (routeDTO != null) {
			Airport newOriginAirport = null;
			AirportDTO originDTO = routeDTO.getOrigin();
			if (originDTO != null) {
				String newOriginIata = originDTO.getIata();
				if (newOriginIata == null || newOriginIata.length() < 2 || newOriginIata.length() > 3) {
					return new ResponseEntity<>(
							"El codigo iata del aeropuerto origen debe contener entre 2 y 3 caracteres",
							HttpStatus.BAD_REQUEST);
				} else {
					newOriginAirport = this.airportService.findByIata(newOriginIata);
					if (newOriginAirport == null) {
						return new ResponseEntity<>("El iata del aeropuerto origen es incorrecto",
								HttpStatus.BAD_REQUEST);
					}
					applyChanges = true;
				}
			} else {
				newOriginAirport = oldRbc.getRoute().getOriginAirport();
			}

			Airport newDestinationAirport = null;
			AirportDTO destinationDTO = routeDTO.getDestination();
			if (destinationDTO != null) {
				String newDestinationIata = destinationDTO.getIata();
				if (newDestinationIata == null || newDestinationIata.length() < 2 || newDestinationIata.length() > 3) {
					return new ResponseEntity<>(
							"El codigo iata del aeropuerto destino debe contener entre 2 y 3 caracteres",
							HttpStatus.BAD_REQUEST);
				} else {
					newDestinationAirport = this.airportService.findByIata(newDestinationIata);
					if (newDestinationAirport == null) {
						return new ResponseEntity<>("El iata del aeropuerto destino es incorrecto",
								HttpStatus.BAD_REQUEST);
					}
					applyChanges = true;
				}
			} else {
				newDestinationAirport = oldRbc.getRoute().getOriginAirport();
			}

			if (applyChanges) { // Las rutas cambiaron, voy a verificar que exista
				Route newRoute = this.routeService.findByOriginAndDestination(newOriginAirport, newDestinationAirport);
				if (newRoute == null) {
					return new ResponseEntity<>("La nueva ruta especifica es inexistente", HttpStatus.BAD_REQUEST);
				} else { // La ruta existe, voy a verificar si existe para la cabina
					RouteByCabin auxRbc;
					if (priceDTO.getCabin() != null) { // Primero me fijo si cambio la cabina
						Long newCabinId = priceDTO.getCabin().getId();
						if (newCabinId == null) {
							return new ResponseEntity<>("El ID de la nueva cabina es obligatoria.",
									HttpStatus.BAD_REQUEST);
						} else {
							Optional<Cabin> newCabin = this.cabinService.findById(newCabinId);
							if (!newCabin.isPresent()) {
								return new ResponseEntity<>("La nueva cabina es inexistente.", HttpStatus.BAD_REQUEST);
							} else { // Cambio la cabina y existe, verifico la existencia de esa ruta por cabina
								auxRbc = this.routeByCabinService.findByCabinAndRoute(newCabin.get(), newRoute);
								if (auxRbc == null) {
									return new ResponseEntity<>(
											"No se admiten vuelos para la nueva ruta y la nueva cabina del tipo "
													+ oldRbc.getCabin().getName(),
											HttpStatus.BAD_REQUEST);
								} else {
									price.setRouteByCabin(auxRbc);
								}
							}
						}
					} else { // No se modifico la cabina, la nueva ruta debera existir con la misma cabina
						auxRbc = this.routeByCabinService.findByCabinAndRoute(oldRbc.getCabin(), newRoute);
						if (auxRbc == null) {
							return new ResponseEntity<>("No se admiten vuelos para la nueva ruta y la cabina del tipo "
									+ oldRbc.getCabin().getName(), HttpStatus.BAD_REQUEST);
						} else {
							price.setRouteByCabin(auxRbc);
						}
					}
				}
			}
		}

		if (!applyChanges && priceDTO.getCabin() != null) { // La ruta no cambio pero se cambio la cabina
			Long newCabinId = priceDTO.getCabin().getId();
			if (newCabinId == null) {
				return new ResponseEntity<>("El ID de la nueva cabina es obligatoria.", HttpStatus.BAD_REQUEST);
			} else {
				Optional<Cabin> newCabin = this.cabinService.findById(newCabinId);
				if (!newCabin.isPresent()) {
					return new ResponseEntity<>("La nueva cabina es inexistente.", HttpStatus.BAD_REQUEST);
				} else { // Cambio la cabina, verifico la existencia de esa ruta por cabina
					RouteByCabin auxRbc = this.routeByCabinService.findByCabinAndRoute(newCabin.get(),
							oldRbc.getRoute());
					if (auxRbc == null) {
						return new ResponseEntity<>("No se admiten vuelos para la nueva cabina y la ruta"
								+ oldRbc.getRoute().getOriginAirport().getIata() + "-"
								+ oldRbc.getRoute().getDestinationAiport().getIata(), HttpStatus.BAD_REQUEST);
					} else {
						price.setRouteByCabin(auxRbc);
						applyChanges = true;
					}
				}
			}
		}
		// Fin validacion super larga y aburrida

		if (priceDTO.getPrice() > 0) { // Nothing is free
			price.setPrice(priceDTO.getPrice());
			applyChanges = true;
		}

		LocalDate fromDate = priceDTO.getFromDate();
		if (fromDate != null) {
			price.setFromDate(fromDate);
			applyChanges = true;
		}

		LocalDate toDate = priceDTO.getToDate();
		if (toDate != null) {
			price.setFromDate(toDate);
			applyChanges = true;
		}

		if (price.getFromDate().isAfter(price.getToDate())) {
			return new ResponseEntity<>("La fecha de inicio no puede ser menor a la fecha de fin",
					HttpStatus.BAD_REQUEST);
		}

		if (applyChanges) {
			try {
				price = this.priceService.save(price);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("No se encontraron modificaciones a realizar", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(this.converter.modelToDTO(price, PriceDTO.class), HttpStatus.OK);
	}

	@GetMapping("/prices/{originIata}/{destinationIata}/{fromDate}/{toDate}")
	public ResponseEntity<?> getPricesByRouteAndDates(@PathVariable("originIata") String originIata,
			@PathVariable("destinationIata") String destinationIata,
			@PathVariable("fromDate") @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
			@PathVariable("toDate") @DateTimeFormat(iso = ISO.DATE) LocalDate toDate) {

		Airport originAirport = this.airportService.findByIata(originIata);
		if (originAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto origen es incorrecto", HttpStatus.BAD_REQUEST);
		}

		Airport destinationAirport = this.airportService.findByIata(destinationIata);
		if (destinationAirport == null) {
			return new ResponseEntity<>("El iata del aeropuerto destino es incorrecto", HttpStatus.BAD_REQUEST);
		}

		if (fromDate.isAfter(toDate)) {
			return new ResponseEntity<>("La fecha de inicio no puede ser menor a la fecha de fin",
					HttpStatus.BAD_REQUEST);
		}
		
		Route route = this.routeService.findByOriginAndDestination(originAirport, destinationAirport);
		if (route == null) {
			return new ResponseEntity<>("La ruta ingresada es inexistente", HttpStatus.BAD_REQUEST);
		}

		List<Price> prices = this.priceService.findByRouteAndDates(route, fromDate, toDate);
		if (prices.isEmpty()) {
			return new ResponseEntity<>(prices, HttpStatus.OK);
		}
		return new ResponseEntity<>(this.converter.modelsToDTOs(prices, PriceDTO.class), HttpStatus.OK);
	}
}
