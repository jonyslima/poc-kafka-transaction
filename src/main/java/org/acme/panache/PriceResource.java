package org.acme.panache;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniJoin;
import io.smallrye.mutiny.groups.UniSubscribe;
import io.smallrye.mutiny.helpers.spies.Spy;
import jakarta.ejb.TransactionAttribute;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.persist;

@Path("/prices")
public class PriceResource {
    @Inject
    @Channel("generated-price")
    Emitter<Price> priceEmitter;
    @Inject
    PriceGenerator priceGenerator;

    @Inject
    PriceRepository priceRepository;

    /**
     * We uses classic Hibernate, so the API is blocking, so we need to use @Blocking.
     *
     * @return the list of prices
     */
    @GET
    public Uni<List<PanacheEntityBase>> getAllPrices() {
        return Price.listAll();
    }


    @POST
    public Response getAllPrices(Price price) {
        priceEmitter.send(price);
        return Response.ok().build();
    }

}
