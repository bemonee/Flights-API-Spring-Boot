package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_origin")
    @JsonManagedReference(value="originRoute-list")
    private Airport airportOrigin;

    @ManyToOne
    @JoinColumn(name = "id_destination")
    @JsonManagedReference(value="destinarionRoute-list")
    private Airport airportDestination;

    public Route() {
    }

    @JsonGetter
    public Airport getAirportOrigin() {
        return airportOrigin;
    }

    @JsonGetter
    public Airport getAirportDestination() {
        return airportDestination;
    }

    @JsonSetter
    public void setAirportOrigin(Airport airportOrigin) {
        this.airportOrigin = airportOrigin;
    }

    @JsonSetter
    public void setAirportDestination(Airport airportDestination) {
        this.airportDestination = airportDestination;
    }
}
