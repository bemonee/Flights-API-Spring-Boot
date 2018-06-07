package com.utn.tp5.configs;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.utn.tp5.dtos.PriceDTO;
import com.utn.tp5.models.Price;

/**
 *
 * @author Ramiro Agustin Pereyra Noreiko <bemonee@gmail.com>
 */
@Configuration
public class Config {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		PropertyMap<Price, PriceDTO> priceMap = new PropertyMap<Price, PriceDTO>() {
			protected void configure() {
				map().getCabin().setId(source.getRouteByCabin().getCabin().getId());
			}
		};

		modelMapper.addMappings(priceMap);
		return modelMapper;
	}
}
