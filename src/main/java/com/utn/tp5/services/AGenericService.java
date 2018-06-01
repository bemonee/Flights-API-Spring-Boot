package com.utn.tp5.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AGenericService<R extends JpaRepository<M, Long>, M> {

	protected R repo;

	public AGenericService(R repo) {
		super();
		this.repo = repo;
	}

	public M getOne(Long id) {
		return this.repo.getOne(id);
	}

	public List<M> findAll() {
		return this.repo.findAll();
	}

}
