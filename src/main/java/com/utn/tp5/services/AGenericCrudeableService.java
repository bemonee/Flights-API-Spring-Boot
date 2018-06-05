package com.utn.tp5.services;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AGenericCrudeableService<R extends JpaRepository<M, Long>, M> extends AGenericService<R, M> {

	public AGenericCrudeableService(R repo) {
		super(repo);
	}

	public M save(M model) throws Exception {
		return this.repo.save(model);
	}

	public void delete(M model) {
		this.repo.delete(model);
	}

}
