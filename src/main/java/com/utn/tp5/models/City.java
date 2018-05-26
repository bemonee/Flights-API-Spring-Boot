package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.*;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
    @JsonManagedReference(value="city-list")
    private State state;

    @OneToMany(mappedBy = "city")
    @JsonBackReference(value="airport-list")
    private List<Airport> airports;

    public City() {
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
    public State getState() {
        return state;
    }

    @JsonGetter
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
