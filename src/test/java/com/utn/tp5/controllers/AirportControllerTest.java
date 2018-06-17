package com.utn.tp5.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.tp5.dtos.CityDTO;
import com.utn.tp5.models.City;
import com.utn.tp5.models.Country;
import com.utn.tp5.models.State;
import com.utn.tp5.services.CityService;
import com.utn.tp5.services.CountryService;
import com.utn.tp5.services.StateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.utn.tp5.converters.GenericConverter;
import com.utn.tp5.dtos.AirportDTO;
import com.utn.tp5.models.Airport;
import com.utn.tp5.services.AirportService;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(AirportController.class)
public class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @MockBean
    private CityService cityService;

    @MockBean
    private StateService stateService;

    @MockBean
    private CountryService countryService;

    @MockBean
    GenericConverter<Airport, AirportDTO> converter;


    @Test
    public void getVerbShouldReturnList() throws Exception {

        List<Airport> airports = new ArrayList<>();
        Airport airport = new Airport();
        airport.setId(new Long(1));
        airport.setName("Astor Piazola");
        airport.setIata("ASF");
        airports.add(airport);

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setName(airport.getName());
        airportDTO.setIata(airport.getIata());

        List<AirportDTO> airportsDTOs = new ArrayList<>();
        airportsDTOs.add(airportDTO);

        Mockito.when(airportService.findAll()).thenReturn(airports);
        Mockito.when(converter.modelsToDTOs(airports, AirportDTO.class)).thenReturn(airportsDTOs);
        ResultActions result = this.mockMvc.perform(get("/api/airports")).andDo(print()).andExpect(status().isOk());

        Mockito.verify(airportService, Mockito.times(1)).findAll();
        Mockito.verify(converter, Mockito.times(1)).modelsToDTOs(airports, AirportDTO.class);

        String responseBody = result.andReturn().getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("[{\"iata\":\"ASF\",\"name\":\"Astor Piazola\",\"city\":null}]");

    }

    @Test
    public void getVerbShouldReturnEmptyList() throws Exception {

        List<Airport> airports = new ArrayList<>();
        Airport airport = new Airport();
        airports.add(airport);

        List<AirportDTO> airportsDTOs = converter.modelsToDTOs(airports, AirportDTO.class);

        Mockito.when(airportService.findAll()).thenReturn(airports);
        Mockito.when(converter.modelsToDTOs(airports, AirportDTO.class)).thenReturn(airportsDTOs);
        ResultActions result = this.mockMvc.perform(get("/api/airports")).andDo(print()).andExpect(status().isOk());

        Mockito.verify(airportService, Mockito.times(1)).findAll();

        String responseBody = result.andReturn().getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("[]");

    }

    @Test
    public void getVerbOriginShouldReturnDestination() throws Exception {

        String iata = "ASD";

        Airport airportOrigin = new Airport();
        airportOrigin.setIata(iata);

        Airport airportDestination = new Airport();
        Set<Airport> destionationAirports = new HashSet<>();
        destionationAirports.add(airportDestination);

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("LOL");
        airportDTO.setName("Jose Luis");

        List<AirportDTO> airportsDtos = new ArrayList<>();
        airportsDtos.add(airportDTO);

        Mockito.when(airportService.findByIata(iata)).thenReturn(airportOrigin);
        Mockito.when(airportService.getDestinationAirportsByOrigin(airportOrigin)).thenReturn(destionationAirports);
        Mockito.when(converter.modelsToDTOs(destionationAirports, AirportDTO.class)).thenReturn(airportsDtos);

        ResultActions result = this.mockMvc.perform(get("/api/airports/ASD/routes")).andDo(print()).andExpect(status().isOk());

        Mockito.verify(airportService, Mockito.times(1)).findByIata(iata);
        Mockito.verify(airportService, Mockito.times(1)).getDestinationAirportsByOrigin(airportOrigin);
        Mockito.verify(converter, Mockito.times(1)).modelsToDTOs(destionationAirports, AirportDTO.class);

        String responseBody = result.andReturn().getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("[{\"iata\":\"LOL\",\"name\":\"Jose Luis\",\"city\":null}]");

    }

    @Test
    public void postVerbShouldReturnDto() throws Exception {

        String iataArrive = "ASD";

        CityDTO cityDTO = new CityDTO();
        cityDTO.setIata("MDQ");
        cityDTO.setName("HOLAAAAA");

        City city = new City();
        city.setIata(cityDTO.getIata());
        city.setName(cityDTO.getName());

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("LOL");
        airportDTO.setName("Jose Luis");
        airportDTO.setCity(cityDTO);

        String iata = airportDTO.getIata();
        String name = airportDTO.getName();

        Mockito.when(airportService.findByIata(iataArrive)).thenReturn(null);
        Mockito.when(cityService.findByIata(cityDTO.getIata())).thenReturn(city);

        Airport airport = converter.DtoToModel(airportDTO, Airport.class);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        Mockito.when(airportService.save(airport)).thenReturn(airport);
        Mockito.when(converter.modelToDTO(airport, AirportDTO.class)).thenReturn(airportDTO);

        ResultActions result = this.mockMvc.perform(post("/api/airports")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Mockito.verify(airportService, Mockito.times(1)).findByIata(iata);
        Mockito.verify(cityService, Mockito.times(1)).findByIata(cityDTO.getIata());
        Mockito.verify(converter, Mockito.times(1)).modelToDTO(airport, AirportDTO.class);

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("{\"iata\":\"LOL\",\"name\":\"Jose Luis\",\"city\":{\"iata\":\"MDQ\",\"name\":\"HOLAAAAA\",\"state\":null}}");


    }

}
