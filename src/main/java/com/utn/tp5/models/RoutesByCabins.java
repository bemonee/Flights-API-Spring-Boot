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
    private Route route;

    @JoinColumn(name = "id_cabin")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cabin cabin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "routeByCabin")
    private List<Price> prices;
}
