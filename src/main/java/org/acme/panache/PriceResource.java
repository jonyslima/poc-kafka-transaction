package org.acme.panache;

import java.util.List;

import jakarta.inject.Inject;
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
    Emitter<Integer> priceEmitter;
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
        priceEmitter.send(numero);
        return numero;
    }
}
