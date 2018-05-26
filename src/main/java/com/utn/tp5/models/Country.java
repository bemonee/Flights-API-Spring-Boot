package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "iso2")
    private String iso2;

    @OneToMany(mappedBy = "country")
    @JsonBackReference
    private List<State> states;

    public Country() {
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonGetter
    public String getIso2() {
        return iso2;
    }

    @JsonGetter
    public List<State> getStates() {
        return states;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter
    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    @JsonSetter
    public void setStates(List<State> states) {
        this.states = states;
    }
}
