package com.utn.tp5.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.utn.tp5.converters.GenericConverter;
import com.utn.tp5.dtos.PriceDTO;
import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Cabin;
import com.utn.tp5.models.Price;
import com.utn.tp5.models.Route;
import com.utn.tp5.services.AirportService;
import com.utn.tp5.services.CabinService;
import com.utn.tp5.services.PriceService;
import com.utn.tp5.services.RouteByCabinService;
import com.utn.tp5.services.RouteService;

@RunWith(SpringRunner.class)
@WebMvcTest(PriceController.class)
public class PriceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PriceService priceService;

	@MockBean
	private AirportService airportService;

	@MockBean
	private CabinService cabinService;

	@MockBean
	private RouteService routeService;

	@MockBean
	private RouteByCabinService routeByCabinService;

	@MockBean
	private GenericConverter<Price, PriceDTO> converter;

	@Test
	public void testGetAll() throws Exception {
		Price price = new Price();
		List<Price> prices = new ArrayList<>();
		prices.add(price);

		PriceDTO priceDto = new PriceDTO();
		List<PriceDTO> pricesDtos = new ArrayList<>();
		pricesDtos.add(priceDto);

		Mockito.when(priceService.findAll()).thenReturn(prices);
		Mockito.when(converter.modelsToDTOs(prices, PriceDTO.class)).thenReturn(pricesDtos);
		ResultActions result = this.mockMvc.perform(get("/api/prices")).andDo(print()).andExpect(status().isOk());

		Mockito.verify(priceService, Mockito.times(1)).findAll();
		Mockito.verify(converter, Mockito.times(1)).modelsToDTOs(prices, PriceDTO.class);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody)
				.isEqualTo("[{\"route\":null,\"cabin\":null,\"price\":0.0,\"toDate\":null,\"fromDate\":null}]");
	}

	@Test
	public void testGetAllEmpty() throws Exception {

		List<Price> prices = new ArrayList<>();

		Mockito.when(priceService.findAll()).thenReturn(prices);
		ResultActions result = this.mockMvc.perform(get("/api/prices")).andDo(print()).andExpect(status().isOk());

		Mockito.verify(priceService, Mockito.times(1)).findAll();

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("[]");
	}

	@Test
	public void testCreate() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");
		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));
		Optional<Cabin> optCabin = Optional.of(cabin);

		Route route = new Route();
		route.setOriginAirport(originAirport);
		route.setDestinationAiport(destinationAirport);

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);

		ObjectMapper mapper = new ObjectMapper();
		String jsonContent = mapper.writeValueAsString(optCabin);

		ResultActions result = this.mockMvc.perform(patch("/api/prices/" + originAirport.getIata())
				.content(jsonContent)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());

		String responseBody = result.andReturn().getResponse().getContentAsString();

		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testDelete() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");
		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));
		Optional<Cabin> optCabin = Optional.of(cabin);

		Route route = new Route();
		route.setOriginAirport(originAirport);
		route.setDestinationAiport(destinationAirport);

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);
		Mockito.when(airportService.findByIata(destinationAirport.getIata())).thenReturn(destinationAirport);
		Mockito.when(cabinService.findById(cabin.getId())).thenReturn(optCabin);
		Mockito.when(routeService.findByOriginAndDestination(originAirport, destinationAirport)).thenReturn(route);
		ResultActions result = this.mockMvc.perform(delete("/api/prices/MDP/AEP/1")).andDo(print())
				.andExpect(status().is(204));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		Mockito.verify(airportService, Mockito.times(1)).findByIata(destinationAirport.getIata());
		Mockito.verify(cabinService, Mockito.times(1)).findById(cabin.getId());
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(originAirport, destinationAirport);
		Mockito.verify(priceService, Mockito.times(1)).delete(route, optCabin.get());

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testDeleteWithInvalidOriginIata() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(null);
		ResultActions result = this.mockMvc.perform(delete("/api/prices/MDP/AEP/1")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("El iata del aeropuerto origen es incorrecto");
	}

	@Test
	public void testDeleteWithInvalidDestinationIata() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");

		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);
		Mockito.when(airportService.findByIata(destinationAirport.getIata())).thenReturn(null);
		ResultActions result = this.mockMvc.perform(delete("/api/prices/MDP/AEP/1")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		Mockito.verify(airportService, Mockito.times(1)).findByIata(destinationAirport.getIata());
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("El iata del aeropuerto destino es incorrecto");
	}

	@Test
	public void testDeleteWithInvalidCabin() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");

		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));

		Optional<Cabin> optCabin = Optional.ofNullable(null);

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);
		Mockito.when(airportService.findByIata(destinationAirport.getIata())).thenReturn(destinationAirport);
		Mockito.when(cabinService.findById(cabin.getId())).thenReturn(optCabin);
		ResultActions result = this.mockMvc.perform(delete("/api/prices/MDP/AEP/1")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		Mockito.verify(airportService, Mockito.times(1)).findByIata(destinationAirport.getIata());
		Mockito.verify(cabinService, Mockito.times(1)).findById(cabin.getId());
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("El ID de la cabina es incorrecta.");
	}

	@Test
	public void testDeleteWithInvalidRoute() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");

		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));
		Optional<Cabin> optCabin = Optional.of(cabin);

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);
		Mockito.when(airportService.findByIata(destinationAirport.getIata())).thenReturn(destinationAirport);
		Mockito.when(cabinService.findById(cabin.getId())).thenReturn(optCabin);
		Mockito.when(routeService.findByOriginAndDestination(originAirport, destinationAirport)).thenReturn(null);
		ResultActions result = this.mockMvc.perform(delete("/api/prices/MDP/AEP/1")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		Mockito.verify(airportService, Mockito.times(1)).findByIata(destinationAirport.getIata());
		Mockito.verify(cabinService, Mockito.times(1)).findById(cabin.getId());
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(originAirport, destinationAirport);
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("La ruta ingresada es inexistente");
	}

	@Test
	public void testDeleteWithDate() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");

		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));
		Optional<Cabin> optCabin = Optional.of(cabin);

		Route route = new Route();
		route.setOriginAirport(originAirport);
		route.setDestinationAiport(destinationAirport);

		LocalDate date = LocalDate.of(2018, 06, 15);

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);
		Mockito.when(airportService.findByIata(destinationAirport.getIata())).thenReturn(destinationAirport);
		Mockito.when(cabinService.findById(cabin.getId())).thenReturn(optCabin);
		Mockito.when(routeService.findByOriginAndDestination(originAirport, destinationAirport)).thenReturn(route);
		ResultActions result = this.mockMvc.perform(delete("/api/prices/MDP/AEP/1?date=2018-06-15")).andDo(print())
				.andExpect(status().is(204));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		Mockito.verify(airportService, Mockito.times(1)).findByIata(destinationAirport.getIata());
		Mockito.verify(cabinService, Mockito.times(1)).findById(cabin.getId());
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(originAirport, destinationAirport);
		Mockito.verify(priceService, Mockito.times(1)).delete(route, optCabin.get(), date);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testDeleteWithInvalidDate() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");

		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));
		Optional<Cabin> optCabin = Optional.of(cabin);

		Route route = new Route();
		route.setOriginAirport(originAirport);
		route.setDestinationAiport(destinationAirport);

		LocalDate date = LocalDate.of(2018, 06, 15);

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);
		Mockito.when(airportService.findByIata(destinationAirport.getIata())).thenReturn(destinationAirport);
		Mockito.when(cabinService.findById(cabin.getId())).thenReturn(optCabin);
		Mockito.when(routeService.findByOriginAndDestination(originAirport, destinationAirport)).thenReturn(route);
		doThrow(new Exception()).when(priceService).delete(route, optCabin.get(), date);
		ResultActions result = this.mockMvc.perform(delete("/api/prices/MDP/AEP/1?date=2018-06-15")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		Mockito.verify(airportService, Mockito.times(1)).findByIata(destinationAirport.getIata());
		Mockito.verify(cabinService, Mockito.times(1)).findById(cabin.getId());
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(originAirport, destinationAirport);
		Mockito.verify(priceService, Mockito.times(1)).delete(route, optCabin.get(), date);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testUpdate() throws Exception {
		Airport originAirport = new Airport();
		originAirport.setIata("MDP");

		Airport destinationAirport = new Airport();
		destinationAirport.setIata("AEP");

		Route route = new Route();
		route.setOriginAirport(originAirport);
		route.setDestinationAiport(destinationAirport);

		LocalDate fromDate = LocalDate.of(2018, 06, 15);
		LocalDate toDate = LocalDate.of(2018, 06, 30);

		Price price = new Price();
		price.setFromDate(fromDate);
		price.setToDate(toDate);

		List<Price> prices = new ArrayList<>();
		prices.add(price);

		PriceDTO priceDto = new PriceDTO();
		priceDto.setFromDate(fromDate);
		priceDto.setToDate(toDate);
		List<PriceDTO> pricesDtos = new ArrayList<>();
		pricesDtos.add(priceDto);

		Mockito.when(airportService.findByIata(originAirport.getIata())).thenReturn(originAirport);
		Mockito.when(airportService.findByIata(destinationAirport.getIata())).thenReturn(destinationAirport);
		Mockito.when(routeService.findByOriginAndDestination(originAirport, destinationAirport)).thenReturn(route);
		Mockito.when(priceService.findByRouteAndDates(route, fromDate, toDate)).thenReturn(prices);
		Mockito.when(converter.modelsToDTOs(prices, PriceDTO.class)).thenReturn(pricesDtos);
		ResultActions result = this.mockMvc.perform(get("/api/prices/MDP/AEP/2018-06-15/2018-06-30")).andDo(print())
				.andExpect(status().is(200));

		Mockito.verify(airportService, Mockito.times(1)).findByIata(originAirport.getIata());
		Mockito.verify(airportService, Mockito.times(1)).findByIata(destinationAirport.getIata());
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(originAirport, destinationAirport);
		Mockito.verify(priceService, Mockito.times(1)).findByRouteAndDates(route, fromDate, toDate);
		Mockito.verify(converter, Mockito.times(1)).modelsToDTOs(prices, PriceDTO.class);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo(
				"[{\"route\":null,\"cabin\":null,\"price\":0.0,\"toDate\":\"2018-06-30\",\"fromDate\":\"2018-06-15\"}]");
	}

	@Test
	public void testGetPricesByRouteAndDates() throws Exception {

	}

}
