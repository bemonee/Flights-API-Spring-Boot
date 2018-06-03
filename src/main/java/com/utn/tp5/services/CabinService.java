package com.utn.tp5.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp5.models.Cabin;
import com.utn.tp5.repositories.CabinRepository;

@Service
public class CabinService extends AGenericCrudeableService<CabinRepository, Cabin> {
	@Autowired
	public CabinService(CabinRepository repo) {
		super(repo);
	}
}
