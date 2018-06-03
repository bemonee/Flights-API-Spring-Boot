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
@Entity(name = "cities")
@NoArgsConstructor
@Getter
@Setter
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "id_state")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private State state;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
	@JsonManagedReference
	private List<Airport> airports;

	private String name;

	private String iata;
}
