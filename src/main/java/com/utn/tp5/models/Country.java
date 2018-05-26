package com.utn.tp5.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "iso2")
    @NotBlank
    private String iso2;

    @OneToMany(mappedBy = "country")
    @JsonBackReference(value="states-list")
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
