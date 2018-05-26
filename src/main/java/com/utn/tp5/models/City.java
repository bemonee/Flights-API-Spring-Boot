package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.*;
import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "iata")
    private String iata;

    @ManyToOne
    @JoinColumn(name = "id_state")
    @JsonManagedReference
    private State state;

    @OneToMany(mappedBy = "city")
    @JsonBackReference
    private List<Airport> airports;

    public City() {
    }

    public String getName() {
        return name;
    }

    public String getIata() {
        return iata;
    }

    public State getState() {
        return state;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter
    public void setIata(String iata) {
        this.iata = iata;
    }
    @JsonSetter
    public void setState(State state) {
        this.state = state;
    }

    @JsonSetter
    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}
