package com.utn.tp5.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utn.tp5.converters.GenericConverter;
import com.utn.tp5.dtos.CabinDTO;
import com.utn.tp5.models.Cabin;
import com.utn.tp5.services.CabinService;

import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
public class CabinController {

	@Autowired
	CabinService cabinService;

	@Autowired
	GenericConverter<Cabin, CabinDTO> converter;

	@GetMapping("/cabins")
	public ResponseEntity<?> getAll() {
		List<Cabin> cabins = this.cabinService.findAll();
		if (cabins.isEmpty()) {
			return new ResponseEntity<>(cabins, HttpStatus.OK); // El recurso existe pero esta vacio, 200
		} else {
			List<CabinDTO> cabinsDTOs;
			cabinsDTOs = this.converter.modelsToDTOs(cabins, CabinDTO.class);
			return new ResponseEntity<>(cabinsDTOs, HttpStatus.OK);
		}
	}

	@PostMapping("/cabins")
	public ResponseEntity<?> create(@Valid @RequestBody CabinDTO cabinDTO) {
		String name = cabinDTO.getName();
		if (name == null) {
			return new ResponseEntity<>("El nombre de la cabina es obligatorio", HttpStatus.BAD_REQUEST);
		} else {
			if (name.length() < 6 || name.length() > 50) {
				return new ResponseEntity<>("El nombre de la cabina debe contener entre 6 y 50 caracteres",
						HttpStatus.BAD_REQUEST);
			}
		}

		Cabin cabin = this.converter.DtoToModel(cabinDTO, Cabin.class);
		try {
			cabin = this.cabinService.save(cabin);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(this.converter.modelToDTO(cabin, CabinDTO.class), HttpStatus.OK);
	}

	@DeleteMapping("/cabins/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		Optional<Cabin> cabin = this.cabinService.findById(id);
		if (!cabin.isPresent()) {
			return new ResponseEntity<>("No existe una cabina con el id especificado", HttpStatus.BAD_REQUEST);
		} else {
			this.cabinService.delete(cabin.get());
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@PatchMapping("/cabins/{id}")
	public ResponseEntity<?> update(@RequestBody CabinDTO cabinDTO, @PathVariable("id") Long id) {
		Optional<Cabin> checkCabin = this.cabinService.findById(id);
		if (!checkCabin.isPresent()) {
			return new ResponseEntity<>("No existe una cabina con el id especificado", HttpStatus.BAD_REQUEST);
		}

		Long idDTO = cabinDTO.getId();
		if (idDTO != null && idDTO != id) {
			return new ResponseEntity<>("No es posible cambiar el id", HttpStatus.BAD_REQUEST);
		}

		String name = cabinDTO.getName();
		if (name == null) {
			return new ResponseEntity<>("El nombre de la cabina es obligatorio", HttpStatus.BAD_REQUEST);
		} else {
			if (name.length() < 6 || name.length() > 50) {
				return new ResponseEntity<>("El nombre de la cabina debe contener entre 6 y 50 caracteres",
						HttpStatus.BAD_REQUEST);
			}
		}

		Cabin cabin = checkCabin.get();
		try {
			cabin.setName(name);
			cabin = this.cabinService.save(cabin);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(this.converter.modelToDTO(cabin, CabinDTO.class), HttpStatus.OK);
	}
}
