package com.utn.tp5.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Ramiro Agustin Pereyra Noreiko <bemonee@gmail.com>
 */
@Entity(name = "routes")
@Getter
@Setter
@NoArgsConstructor
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "origin_airport_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Airport originAirport;

	@JoinColumn(name = "destination_airport_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Airport destinationAiport;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "route", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<RouteByCabin> routesByCabins;
}
