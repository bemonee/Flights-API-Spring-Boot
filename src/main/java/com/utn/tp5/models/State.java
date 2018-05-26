package com.utn.tp5.models;


import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "states")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "iata")
    private String iata;

    @ManyToOne
    @JoinColumn(name = "id_country")
    @JsonManagedReference(value="states-list")
    private Country country;

    @OneToMany(mappedBy = "state")
    @JsonBackReference(value="city-list")
    private List<City> cities;

    public State() {
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonGetter
    public String getIata() {
        return iata;
    }

    @JsonGetter
    public Country getCountry() {
        return country;
    }

    @JsonGetter
    public List<City> getCities() {
        return cities;
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
    public void setCountry(Country country) {
        this.country = country;
    }

    @JsonSetter
    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
