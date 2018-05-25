package com.utn.tp5.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class CabinRoutes {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "id_cabin")
    private int id_cabin;

    @Column(name = "id_route")
    private int id_route;

    public CabinRoutes(int cabin, int route) {
        this.id_cabin = cabin;
        this.id_route = route;
    }
}
