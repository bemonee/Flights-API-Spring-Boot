package com.utn.tp5.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Cabin;
import com.utn.tp5.models.Price;
import com.utn.tp5.models.Route;
import com.utn.tp5.models.RouteByCabin;
import com.utn.tp5.repositories.PriceRepository;

@Service
public class PriceService extends AGenericCrudeableService<PriceRepository, Price> {

	@Autowired
	RouteByCabinService routeByCabinService;

	@Autowired
	public PriceService(PriceRepository repo) {
		super(repo);
	}

	public Price findPriceByCabinAndRouteAndDate(Route route, Cabin cabin, LocalDate date) {
		Price needle = this.initAuxiliarPrice(route, cabin, date);
		return this.getPriceInSameDates(needle);
	}
	
	

	@Override
	public Price save(Price price) throws Exception {
		if (this.alreadyExistPriceInSameDates(price)) {
			throw new Exception("Ya existe un precio para la misma ruta y cabina en esas fechas");
		}
		return super.save(price);
	}

	public Price save(Price price, Route route, Cabin cabin) throws Exception {
		if (price.getRouteByCabin() == null) {
			RouteByCabin routeByCabin = this.routeByCabinService.findByCabinAndRoute(cabin, route);
			if (routeByCabin != null) {
				price.setRouteByCabin(routeByCabin);
			} else {
				routeByCabin = new RouteByCabin();
				routeByCabin.setRoute(route);
				routeByCabin.setCabin(cabin);
				routeByCabin = this.routeByCabinService.save(routeByCabin);
				price.setRouteByCabin(routeByCabin);
			}

			if (this.alreadyExistPriceInSameDates(price)) {
				throw new Exception("Ya existe un precio para la misma ruta y cabina en esas fechas");
			}
		}

		return super.save(price);
	}

	public void delete(Route route, Cabin cabin) throws Exception {
		RouteByCabin routeByCabin = this.routeByCabinService.findByCabinAndRoute(cabin, route);
		if (routeByCabin != null) {
			List<Price> prices = routeByCabin.getPrices();
			if (!prices.isEmpty()) {
				for (Price p : prices) {
					super.delete(p);
				}
			}
		} else {
			throw new Exception("No existen precios para la ruta y cabina especificada");
		}
	}

	public void delete(Route route, Cabin cabin, LocalDate date) throws Exception {
		Price needle = this.initAuxiliarPrice(route, cabin, date);
		if (needle.getRouteByCabin() != null) {
			Price price = this.getPriceInSameDates(needle);
			if (price != null) {
				super.delete(price);
			} else {
				throw new Exception("No existen precios para la ruta, cabina y fecha especificada");
			}
		} else {
			throw new Exception("No existen precios para la ruta y cabina especificada");
		}
	}

	private Price getPriceInSameDates(Price price) {
		List<Price> prices = price.getRouteByCabin().getPrices();
		if (!prices.isEmpty()) {
			LocalDate from = price.getFromDate();
			LocalDate to = price.getToDate();
			LocalDate pFrom;
			LocalDate pTo;
			for (Price p : prices) {
				pFrom = p.getFromDate();
				pTo = p.getToDate();
				if (pFrom.isEqual(from) && pTo.isEqual(to)) {
					return p;
				} else {
					if (pFrom.isBefore(from) && pTo.isAfter(from)) { // La fecha desde esta dentro de la fecha de inicio
																		// y de fin de otro precio
						return p;
					}
					if (pFrom.isBefore(to) && pTo.isAfter(to)) { // La fecha hasta esta dentro de la fecha de inicio y
																	// de fin de otro precio
						return p;
					}
					if (from.isEqual(pFrom) || to.isEqual(pFrom)) { // La fecha desde o hasta pisa la fecha desde de
																	// otro precio
						return p;
					}
					if (from.isEqual(pTo) || to.isEqual(pTo)) { // la fecha desde o hasta pisa la fecha hasta de otro
																// precio
						return p;
					}
				}
			}
		}
		return null;
	}

	public boolean alreadyExistPriceInSameDates(Price price) {
		return (this.getPriceInSameDates(price) != null);
	}
	
	private Price initAuxiliarPrice(Route route, Cabin cabin, LocalDate date) {
		Price price = new Price();
		price.setFromDate(date);
		price.setToDate(date);
		RouteByCabin routeByCabin = this.routeByCabinService.findByCabinAndRoute(cabin, route);
		price.setRouteByCabin(routeByCabin);
		return price;
	}
	
	public List<Price> findByRouteAndDates(Route route, LocalDate fromDate, LocalDate toDate){
		return this.repo.findByRouteAndDates(route, fromDate, toDate);
	}
}
