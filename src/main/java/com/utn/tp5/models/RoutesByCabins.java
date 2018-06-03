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
@Entity(name = "routes_by_cabins")
@NoArgsConstructor
@Getter
@Setter
public class RoutesByCabins {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "id_route")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Route route;

	@JoinColumn(name = "id_cabin")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Cabin cabin;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "routeByCabin")
	@JsonManagedReference
	private List<Price> prices;
}
