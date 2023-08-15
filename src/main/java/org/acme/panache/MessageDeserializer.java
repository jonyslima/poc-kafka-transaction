package org.acme.panache;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageDeserializer extends JsonbDeserializer<Price> {
    public MessageDeserializer() {
        super(Price.class);
    }
}
