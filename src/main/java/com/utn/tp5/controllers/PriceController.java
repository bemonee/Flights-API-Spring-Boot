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
import com.utn.tp5.services.AirportService;
import com.utn.tp5.services.CabinService;
import com.utn.tp5.services.PriceService;
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

	@DeleteMapping("/prices/routes/{originIata}/{destinationIata}/cabin/{cabinId}")
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

	@PatchMapping("/prices/{id}")
	public ResponseEntity<?> update(@RequestBody PriceDTO priceDTO, @PathVariable("id") Long id) {
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
