package com.utn.tp5.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void getVerbOriginShouldReturnDestination() throws Exception {

    }

}
