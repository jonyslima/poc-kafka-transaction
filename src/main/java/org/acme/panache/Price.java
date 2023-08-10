package org.acme.panache;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Price extends PanacheEntity {

    @Column(unique = true)
    public int value;


    public Price() {
    }

    public Price(Price price) {
        this.value = price.value;
    }
}
