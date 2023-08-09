package org.acme.panache;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class PriceGenerator {

    private Random random = new Random();

    List<Integer> numbers = Collections.synchronizedList(new LinkedList<>());


//    @Outgoing("generated-price")
//    public Multi<Integer> generate() {
//        Multi.createFrom().ticks().every(Duration.ofSeconds(1)).
//        return Multi.createFrom().ticks().every(Duration.ofSeconds(5));
//
//
////        return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
////                .map(tick -> random.nextInt(100));
//    }


    public void push(int number){
        numbers.add(number);
    }
}
