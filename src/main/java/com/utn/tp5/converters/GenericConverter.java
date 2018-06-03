package com.utn.tp5.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericConverter<M, DTO> {

	@Autowired
	ModelMapper modelMapper;

	public DTO modelToDTO(M model, Class<DTO> dtoClass) {
		return this.modelMapper.map(model, dtoClass);
	}

	public List<DTO> modelsToDTOs(Collection<M> models, Class<DTO> dtoClass) {
		return models.stream().map(model -> this.modelMapper.map(model, dtoClass)).collect(Collectors.toList());
	}

	public M DtoToModel(DTO dto, Class<M> modelClass) {
		return this.modelMapper.map(dto, modelClass);
	}
}
