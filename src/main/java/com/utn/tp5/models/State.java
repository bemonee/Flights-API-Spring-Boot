package com.utn.tp5.models;


import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
@Setter
@Getter
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "iata")
    private String iata;

    @Column(name = "id_country")
    private int id_country;

    public State(String name, String iata, int id_country) {
        this.name = name;
        this.iata = iata;
        this.id_country = id_country;
    }
}
