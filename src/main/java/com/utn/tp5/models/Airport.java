package com.utn.tp5.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<Route> originRoutes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "destinationAiport")
    private List<Route> destinationRoutes;

    @JoinColumn(name = "id_city")
    @ManyToOne(fetch = FetchType.LAZY)
    private City city;
}
