package org.acme.panache;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class Service {


    public void process(Price price) {
        price.persist();
    }
}
