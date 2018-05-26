package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cabinRoutes")
public class CabinRoutes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "id_cabin")
    private Cabin cabin;

    @OneToOne
    @JoinColumn(name = "id_route")
    private Route route;

    public CabinRoutes() {
    }

    public Cabin getCabin() {
        return cabin;
    }

    public Route getRoute() {
        return route;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
