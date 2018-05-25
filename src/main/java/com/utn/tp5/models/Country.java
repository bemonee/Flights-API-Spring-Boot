package com.utn.tp5.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Setter
@Getter
public class Country {
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

    public Country(String name, String iata, int id_country) {
        this.name = name;
        this.iata =iata;
        this.id_country = id_country;
    }
}
