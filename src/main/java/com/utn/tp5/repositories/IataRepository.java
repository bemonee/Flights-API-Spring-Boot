package com.utn.tp5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IataRepository<M, ID> extends JpaRepository<M, ID> {
	public M findByIataIgnoreCase(String iata);
}
