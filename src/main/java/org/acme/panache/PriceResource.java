package org.acme.panache;

import java.util.List;
import java.util.concurrent.CompletionStage;

import io.smallrye.reactive.messaging.kafka.transactions.KafkaTransactions;
import io.smallrye.reactive.messaging.kafka.transactions.KafkaTransactionsFactory;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import io.smallrye.common.annotation.Blocking;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/prices")
public class PriceResource {
    @Inject
    @Channel("generated-price")
    Emitter<Price> priceEmitter;
    @Inject
    PriceGenerator priceGenerator;


    /**
     * We uses classic Hibernate, so the API is blocking, so we need to use @Blocking.
     *
     * @return the list of prices
     */
    @GET
    @Blocking
    public List<Price> getAllPrices() {
        return Price.listAll();
    }

    @POST
    @Path("/{numero}")
    public int getAllPrices(int numero) {
        Price price = new Price();
        price.value = numero;
        priceEmitter.send(price);
        return numero;
    }
}
