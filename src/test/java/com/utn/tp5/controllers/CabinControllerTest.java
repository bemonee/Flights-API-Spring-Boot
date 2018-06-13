package com.utn.tp5.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.utn.tp5.converters.GenericConverter;
import com.utn.tp5.dtos.CabinDTO;
import com.utn.tp5.models.Cabin;
import com.utn.tp5.services.CabinService;

@RunWith(SpringRunner.class)
@WebMvcTest(CabinController.class)
public class CabinControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CabinService cabinService;
	
	@MockBean
	GenericConverter<Cabin, CabinDTO> converter;
	
	@Test
	public void getVerbShouldReturnList() throws Exception {

		List<Cabin> cabins = new ArrayList<>();
		Cabin cabin = new Cabin();
		cabin.setId(new Long(1));
		cabin.setName("Turista");
		cabins.add(cabin);

		List<CabinDTO> cabinDtos = new ArrayList<>();
		CabinDTO cabinDTO = new CabinDTO();
		cabinDTO.setId(new Long(1));
		cabinDTO.setName("Turista");
		cabinDtos.add(cabinDTO);

		Mockito.when(cabinService.findAll()).thenReturn(cabins);
		Mockito.when(converter.modelsToDTOs(cabins, CabinDTO.class)).thenReturn(cabinDtos);
		ResultActions result = this.mockMvc.perform(get("/api/cabins")).andDo(print()).andExpect(status().isOk());
		
		Mockito.verify(cabinService, Mockito.times(1)).findAll();
		Mockito.verify(converter, Mockito.times(1)).modelsToDTOs(cabins, CabinDTO.class);
		
		String responseBody = result.andReturn().getResponse().getContentAsString();
		assertThat(responseBody).isEqualTo("[{\"id\":1,\"name\":\"Turista\"}]");

	}

}
