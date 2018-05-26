package com.utn.tp5.models;


import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.util.List;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToMany(mappedBy = "airportOrigin")
    @JsonBackReference(value="originRoute-list")
    private List<Route> originRoutes;

    @OneToMany(mappedBy = "airportDestination")
    @JsonBackReference(value="destinarionRoute-list")
    private List<Route> destinationRoutes;

    @ManyToOne
    @JsonManagedReference(value="airport-list")
    @JoinColumn(name = "id_city")
    private City city;

    public Airport() {
    }

    @JsonGetter
    public List<Route> getOriginRoutes() {
        return originRoutes;
    }

    @JsonGetter
    public List<Route> getDestinationRoutes() {
        return destinationRoutes;
    }

    @JsonGetter
    public City getCity() {
        return city;
    }

    @JsonSetter
    public void setOriginRoutes(List<Route> originRoutes) {
        this.originRoutes = originRoutes;
    }

    @JsonSetter
    public void setDestinationRoutes(List<Route> destinationRoutes) {
        this.destinationRoutes = destinationRoutes;
    }

    @JsonSetter
    public void setCity(City city) {
        this.city = city;
    }
}
