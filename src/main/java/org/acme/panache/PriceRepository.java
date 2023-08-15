package org.acme.panache;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PriceRepository implements PanacheRepositoryBase<Price, Long> {
}
