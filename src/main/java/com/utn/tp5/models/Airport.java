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
@Entity(name = "airports")
@NoArgsConstructor
@Getter
@Setter
public class Airport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String iata;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "originAirport")
	@JsonManagedReference
	private List<Route> originRoutes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "destinationAiport")
	@JsonManagedReference
	private List<Route> destinationRoutes;

	@JoinColumn(name = "id_city")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private City city;
}
