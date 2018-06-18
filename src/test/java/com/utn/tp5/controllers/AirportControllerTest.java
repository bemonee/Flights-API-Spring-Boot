package com.utn.tp5.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.tp5.dtos.CityDTO;
import com.utn.tp5.models.City;
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

    @Test
    public void postVerbShouldReturnBadRequestIfIataWrong() throws Exception {

        CityDTO cityDTO = new CityDTO();
        cityDTO.setIata("MDQ");
        cityDTO.setName("HOLAAAAA");

        City city = new City();
        city.setIata(cityDTO.getIata());
        city.setName(cityDTO.getName());

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("LOLLLLL");
        airportDTO.setName("Jose Luis");
        airportDTO.setCity(cityDTO);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(post("/api/airports")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("El codigo iata debe contener entre 2 y 3 caracteres");

    }

    @Test
    public void postVerbShouldReturnConflictIfAirportExists() throws Exception {


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

        Airport airport = new Airport();
        airport.setIata(airportDTO.getIata());

        Mockito.when(airportService.findByIata(iata)).thenReturn(airport);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(post("/api/airports")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isConflict());

        Mockito.verify(airportService, Mockito.times(1)).findByIata(iata);

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("Ya existe un aeropuerto con ese codigo iata");

    }

    @Test
    public void postVerbShouldReturnBadRequestIfBadAirportName() throws Exception {

        String iataArrive = "ASD";

        CityDTO cityDTO = new CityDTO();
        cityDTO.setIata("MDQ");
        cityDTO.setName("HOLAAAAA");

        City city = new City();
        city.setIata(cityDTO.getIata());
        city.setName(cityDTO.getName());

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("LOL");
        airportDTO.setName("J");
        airportDTO.setCity(cityDTO);

        Mockito.when(airportService.findByIata(iataArrive)).thenReturn(null);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(post("/api/airports")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("El nombre es debe contener entre 6 y 50 caracteres");

    }

    @Test
    public void postVerbShouldReturnBadRequestIfNullCity() throws Exception {

        String iataArrive = "ASD";

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("LOL");
        airportDTO.setName("Jose Luis");

        String iata = airportDTO.getIata();

        Mockito.when(airportService.findByIata(iataArrive)).thenReturn(null);
        Mockito.when(cityService.findByIata(iata)).thenReturn(null);

        Airport airport = converter.DtoToModel(airportDTO, Airport.class);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        Mockito.when(airportService.save(airport)).thenReturn(airport);
        Mockito.when(converter.modelToDTO(airport, AirportDTO.class)).thenReturn(airportDTO);

        ResultActions result = this.mockMvc.perform(post("/api/airports")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("La ciudad es obligatoria");

    }

    @Test
    public void postVerbShouldReturnBadRequestIfCityIata() throws Exception {

        String iataArrive = "ASD";

        CityDTO cityDTO = new CityDTO();
        cityDTO.setIata("M");
        cityDTO.setName("HOLAAAAA");

        City city = new City();
        city.setIata(cityDTO.getIata());
        city.setName(cityDTO.getName());

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("LOL");
        airportDTO.setName("Jose Luis");
        airportDTO.setCity(cityDTO);

        Mockito.when(airportService.findByIata(iataArrive)).thenReturn(null);
        Mockito.when(cityService.findByIata(cityDTO.getIata())).thenReturn(city);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(post("/api/airports")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("El codigo iata de la ciudad debe contener entre 2 y 3 caracteres");

    }

    @Test
    public void postVerbShouldReturnBadRequestIfInvalidCityIata() throws Exception {

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

        Mockito.when(airportService.findByIata(iataArrive)).thenReturn(null);
        Mockito.when(cityService.findByIata(cityDTO.getIata())).thenReturn(null);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(post("/api/airports")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("El codigo iata de la ciudad es invalido");

    }

    @Test
    public void deleteVerbShouldReturnResponse() throws Exception {

        String iata = "HOL";
        Airport airport = new Airport();
        airport.setName("Turista");
        airport.setIata(iata);

        Mockito.when(airportService.findByIata(iata)).thenReturn(airport);

        this.mockMvc.perform(delete("/api/airports/" + iata)).andDo(print()).andExpect(status().isNoContent());

        Mockito.verify(airportService, Mockito.times(1)).delete(airport);

    }

    @Test
    public void patchVerbShouldReturnBadRequestIfAirportDoesntExist() throws Exception {

        String iataArrive = "AFD";

        CityDTO cityDTO = new CityDTO();
        cityDTO.setIata("MDQ");
        cityDTO.setName("HOLAAAAA");

        City city = new City();
        city.setIata(cityDTO.getIata());
        city.setName(cityDTO.getName());

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("AFD");
        airportDTO.setName("Jose Luis");
        airportDTO.setCity(cityDTO);

        String iata = airportDTO.getIata();
        String name = airportDTO.getName();

        Airport airport = new Airport();
        airport.setIata(airportDTO.getIata());
        airport.setName(airportDTO.getName());

        Airport airport2 = new Airport();
        airport2.setIata("ASD");
        airport2.setName("Honolulu");

        Mockito.when(airportService.findByIata(iataArrive)).thenReturn(null);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(patch("/api/airports/" + iata)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("No existe un aeropuerto con el codigo iata especificado");

    }

    @Test
    public void patchVerbShouldReturnConflictIfAirportExists() throws Exception {

        String iataArrive = "AFD";

        CityDTO cityDTO = new CityDTO();
        cityDTO.setIata("MDQ");
        cityDTO.setName("HOLAAAAA");

        City city = new City();
        city.setIata(cityDTO.getIata());
        city.setName(cityDTO.getName());

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("AFD");
        airportDTO.setName("Jose Luis");
        airportDTO.setCity(cityDTO);

        String iata = airportDTO.getIata();

        Airport airport = new Airport();
        airport.setIata(airportDTO.getIata());
        airport.setName(airportDTO.getName());

        Airport airport2 = new Airport();
        airport2.setIata("ASD");
        airport2.setName("Honolulu");

        Mockito.when(airportService.findByIata(iataArrive)).thenReturn(airport);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(patch("/api/airports/" + iata)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isConflict());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("Ya existe un aeropuerto con ese codigo iata");

    }

    @Test
    public void patchVerbShouldReturnBadRequestIfInvalidIata() throws Exception {

        CityDTO cityDTO = new CityDTO();
        cityDTO.setIata("MDQ");
        cityDTO.setName("HOLAAAAA");

        City city = new City();
        city.setIata(cityDTO.getIata());
        city.setName(cityDTO.getName());

        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setIata("AFDDDDDDDD");
        airportDTO.setName("Jose Luis");
        airportDTO.setCity(cityDTO);

        String iata = airportDTO.getIata();

        Airport airport = new Airport();
        airport.setIata(airportDTO.getIata());
        airport.setName(airportDTO.getName());

        Airport airport2 = new Airport();
        airport2.setIata("ASD");
        airport2.setName("Honolulu");

        Mockito.when(airportService.findByIata(airportDTO.getIata())).thenReturn(airport);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(airportDTO);

        ResultActions result = this.mockMvc.perform(patch("/api/airports/" + iata)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo("El codigo iata debe contener entre 2 y 3 caracteres");

    }


}
