package com.utn.tp5.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utn.tp5.models.Price;
import com.utn.tp5.models.Route;

public interface PriceRepository extends JpaRepository<Price, Long> {
	public List<Price> findByRouteByCabinId(Long id);

	@Query("SELECT p FROM prices AS p INNER JOIN FETCH p.routeByCabin rbc WHERE rbc.route = :route AND (p.fromDate <= :fromDate and p.toDate >= :toDate)")
	public List<Price> findByRouteAndDates(@Param("route") Route route, @Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);
}
