package com.utn.tp5.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utn.tp5.models.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {
	public List<Price> findByRouteByCabinId(Long id);
}
