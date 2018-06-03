package com.utn.tp5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utn.tp5.models.Cabin;

@Repository
public interface CabinRepository extends JpaRepository<Cabin, Long> {

}
