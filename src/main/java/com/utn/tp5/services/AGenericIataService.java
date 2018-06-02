package com.utn.tp5.services;

import com.utn.tp5.repositories.IataRepository;

public abstract class AGenericIataService<R extends IataRepository<M, Long>, M> extends AGenericCrudeableService<R, M> {

	public AGenericIataService(R repo) {
		super(repo);
	}
	
	public M findByIata(String iata) {
		return this.repo.findByIataIgnoreCase(iata);
	}
}
