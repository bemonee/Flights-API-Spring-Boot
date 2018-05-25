package com.utn.tp5.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@Setter
@Getter
public class Price {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "from")
    private Date from;

    @Column(name = "to")
    private Date to;

    @Column(name = "price")
    private double price;

    public Price(Date from, Date to, double price) {
        this.from = from;
        this.to = to;
        this.price = price;
    }

}
