package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cabins")
public class Cabin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne(mappedBy = "cabin")
    @JoinColumn(name = "id_cabin")
    private CabinRoutes cabin;

    @Column(name = "name")
    private String name;

    public Cabin() {
    }

    @JsonGetter
    public CabinRoutes getCabin() {
        return cabin;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonSetter
    public void setCabin(CabinRoutes cabin) {
        this.cabin = cabin;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }


}
