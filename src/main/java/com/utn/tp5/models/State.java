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
@Entity(name = "states")
@NoArgsConstructor
@Getter
@Setter
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "id_country")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Country country;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
	@JsonManagedReference
	private List<City> cities;

	private String name;

	private String iata;
}
