package org.acme.panache;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Price extends PanacheEntity {

    @Column(unique = true)
    public int value;


    public Price() {
    }

    public Price(int value) {
        this.value = value;
    }

    public Price(Price price) {
        this.value = price.value;
    }


    @Override
    public <T extends PanacheEntityBase> Uni<T> persist() {
        return super.persist();
    }

    @Override
    public String toString() {
        return "Price{" +
                "value=" + value +
                ", id=" + id +
                '}';
    }
}
