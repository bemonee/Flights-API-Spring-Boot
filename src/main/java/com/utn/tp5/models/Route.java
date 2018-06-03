package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "route")
	@JsonManagedReference
	private List<RoutesByCabins> routesByCabins;
}
