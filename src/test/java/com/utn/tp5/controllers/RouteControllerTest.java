package com.utn.tp5.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.tp5.converters.GenericConverter;
import com.utn.tp5.dtos.AirportDTO;
import com.utn.tp5.dtos.RouteDTO;
import com.utn.tp5.models.Airport;
import com.utn.tp5.models.Route;
import com.utn.tp5.services.AirportService;
import com.utn.tp5.services.RouteService;

@RunWith(SpringRunner.class)
@WebMvcTest(RouteController.class)
public class RouteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RouteService routeService;

	@MockBean
	private AirportService airportService;

	@MockBean
	GenericConverter<Route, RouteDTO> converter;

	@Test
	public void testCreate() throws Exception {
		RouteDTO routeDto = new RouteDTO();
		AirportDTO originDto = new AirportDTO();
		originDto.setIata("MDP");
		AirportDTO destinationDto = new AirportDTO();
		destinationDto.setIata("AEP");
		routeDto.setOrigin(originDto);
		routeDto.setDestination(destinationDto);

		Airport origin = new Airport();
		origin.setIata("MDP");
		Airport destination = new Airport();
		destination.setIata("AEP");
		Route newRoute = new Route();
		newRoute.setOriginAirport(origin);
		newRoute.setDestinationAiport(destination);

		Route anotherRoute = new Route();
		newRoute.setOriginAirport(origin);
		newRoute.setDestinationAiport(destination);

		Mockito.when(airportService.findByIata("MDP")).thenReturn(origin);
		Mockito.when(airportService.findByIata("AEP")).thenReturn(destination);
		Mockito.when(routeService.findByOriginAndDestination(origin, destination)).thenReturn(null);
		Mockito.when(routeService.save(newRoute)).thenReturn(anotherRoute);
		Mockito.when(converter.modelToDTO(anotherRoute, RouteDTO.class)).thenReturn(routeDto);

		ObjectMapper mapper = new ObjectMapper();
		String jsonContent = mapper.writeValueAsString(routeDto);

		ResultActions result = this.mockMvc.perform(post("/api/routes").content(jsonContent)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDP");
		Mockito.verify(airportService, Mockito.times(1)).findByIata("AEP");
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(origin, destination);
		Mockito.verify(routeService, Mockito.times(1)).save(Mockito.any());
		Mockito.verify(converter, Mockito.times(1)).modelToDTO(Mockito.any(), Mockito.any());

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testGetAllAirports() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");
		Airport destination = new Airport();
		destination.setIata("AEP");
		Route route = new Route();
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);
		List<Route> routes = new ArrayList<>();
		routes.add(route);

		AirportDTO originDto = new AirportDTO();
		originDto.setIata("MDQ");
		AirportDTO destinationDto = new AirportDTO();
		destinationDto.setIata("AEP");
		RouteDTO routeDto = new RouteDTO();
		routeDto.setOrigin(originDto);
		routeDto.setDestination(destinationDto);
		List<RouteDTO> routeDtos = new ArrayList<>();
		routeDtos.add(routeDto);

		Mockito.when(routeService.findAll()).thenReturn(routes);
		Mockito.when(converter.modelsToDTOs(routes, RouteDTO.class)).thenReturn(routeDtos);
		ResultActions result = this.mockMvc.perform(get("/api/routes")).andDo(print()).andExpect(status().isOk());

		Mockito.verify(routeService, Mockito.times(1)).findAll();
		Mockito.verify(converter, Mockito.times(1)).modelsToDTOs(routes, RouteDTO.class);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo(
				"[{\"origin\":{\"iata\":\"MDQ\",\"name\":null,\"city\":null},\"destination\":{\"iata\":\"AEP\",\"name\":null,\"city\":null}}]");
	}

	@Test
	public void testGetAllAirportsEmpty() throws Exception {
		List<Route> routes = new ArrayList<>();

		Mockito.when(routeService.findAll()).thenReturn(routes);
		ResultActions result = this.mockMvc.perform(get("/api/routes")).andDo(print()).andExpect(status().isOk());

		Mockito.verify(routeService, Mockito.times(1)).findAll();

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("[]");
	}

	@Test
	public void testGetRouteByOriginAndDestinationAirport() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");

		Airport destination = new Airport();
		destination.setIata("AEP");

		Route route = new Route();
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);

		RouteDTO routeDto = new RouteDTO();
		AirportDTO originDto = new AirportDTO();
		originDto.setIata("MDQ");
		AirportDTO destinationDto = new AirportDTO();
		destinationDto.setIata("AEP");
		routeDto.setOrigin(originDto);
		routeDto.setDestination(destinationDto);

		Mockito.when(airportService.findByIata("MDQ")).thenReturn(origin);
		Mockito.when(airportService.findByIata("AEP")).thenReturn(destination);
		Mockito.when(routeService.findByOriginAndDestination(origin, destination)).thenReturn(route);
		Mockito.when(converter.modelToDTO(route, RouteDTO.class)).thenReturn(routeDto);

		ResultActions result = this.mockMvc.perform(get("/api/routes/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().isOk());

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");
		Mockito.verify(airportService, Mockito.times(1)).findByIata("AEP");
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(origin, destination);
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo(
				"{\"origin\":{\"iata\":\"MDQ\",\"name\":null,\"city\":null},\"destination\":{\"iata\":\"AEP\",\"name\":null,\"city\":null}}");
	}

	@Test
	public void testGetRouteByOriginAndDestinationAirportWithNullOrigin() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");

		Mockito.when(airportService.findByIata("MDQ")).thenReturn(null);

		ResultActions result = this.mockMvc.perform(get("/api/routes/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().is(404));

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testGetRouteByOriginAndDestinationAirportWithNullDestination() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");

		Mockito.when(airportService.findByIata("MDQ")).thenReturn(origin);
		Mockito.when(airportService.findByIata("AEP")).thenReturn(null);

		ResultActions result = this.mockMvc.perform(get("/api/routes/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().is(404));

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");
		Mockito.verify(airportService, Mockito.times(1)).findByIata("AEP");
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testGetRouteByOriginAndDestinationAirportWithNullRoute() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");

		Airport destination = new Airport();
		destination.setIata("AEP");

		Mockito.when(airportService.findByIata("MDQ")).thenReturn(origin);
		Mockito.when(airportService.findByIata("AEP")).thenReturn(destination);
		Mockito.when(routeService.findByOriginAndDestination(origin, destination)).thenReturn(null);

		ResultActions result = this.mockMvc.perform(get("/api/routes/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().is(404));

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");
		Mockito.verify(airportService, Mockito.times(1)).findByIata("AEP");
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(origin, destination);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testDeleteHappyPath() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");

		Airport destination = new Airport();
		destination.setIata("AEP");

		Route route = new Route();
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);

		Mockito.when(airportService.findByIata("MDQ")).thenReturn(origin);
		Mockito.when(airportService.findByIata("AEP")).thenReturn(destination);
		Mockito.when(routeService.findByOriginAndDestination(origin, destination)).thenReturn(route);

		ResultActions result = this.mockMvc.perform(delete("/api/route/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().is(204));

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");
		Mockito.verify(airportService, Mockito.times(1)).findByIata("AEP");
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(origin, destination);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("");
	}

	@Test
	public void testDeleteWithNullOrigin() throws Exception {
		Mockito.when(airportService.findByIata("MDQ")).thenReturn(null);
		ResultActions result = this.mockMvc.perform(delete("/api/route/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("No existe un aeropuerto de origen con el codigo iata especificado");
	}

	@Test
	public void testDeleteWithNullDestination() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");

		Mockito.when(airportService.findByIata("MDQ")).thenReturn(origin);
		Mockito.when(airportService.findByIata("AEP")).thenReturn(null);
		ResultActions result = this.mockMvc.perform(delete("/api/route/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("No existe un aeropuerto de destino con el codigo iata especificado");
	}

	@Test
	public void testDeleteWithNullRoute() throws Exception {
		Airport origin = new Airport();
		origin.setIata("MDQ");

		Airport destination = new Airport();
		destination.setIata("AEP");

		Route route = new Route();
		route.setOriginAirport(origin);
		route.setDestinationAiport(destination);

		Mockito.when(airportService.findByIata("MDQ")).thenReturn(origin);
		Mockito.when(airportService.findByIata("AEP")).thenReturn(destination);
		Mockito.when(routeService.findByOriginAndDestination(origin, destination)).thenReturn(null);

		ResultActions result = this.mockMvc.perform(delete("/api/route/origin/MDQ/destination/AEP")).andDo(print())
				.andExpect(status().is(400));

		Mockito.verify(airportService, Mockito.times(1)).findByIata("MDQ");
		Mockito.verify(airportService, Mockito.times(1)).findByIata("AEP");
		Mockito.verify(routeService, Mockito.times(1)).findByOriginAndDestination(origin, destination);

		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("No existe la ruta con esa iata de origen y esa de destino");
	}

	@Test
	public void testUpdate() {

	}

}
