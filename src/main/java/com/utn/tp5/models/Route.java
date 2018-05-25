package com.utn.tp5.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Setter
@Getter
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "origin_airport_id")
    private int origin_airport_id;

    @Column(name = "destination_airport_id")
    private int destination_airport_id;

    public Route(int origin, int destination) {
        this.origin_airport_id = origin;
        this.destination_airport_id = destination;
    }
}
