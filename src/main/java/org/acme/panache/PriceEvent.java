package org.acme.panache;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ejb.TransactionAttribute;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.*;

import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class PriceEvent {
    @Inject
    @Channel("cube-price")
    Emitter<Price> cubePriceEmitter;
    @Inject
    PriceRepository priceRepository;

//    @Incoming("prices")
//    @TransactionAttribute
////    @WithSession
////    @WithTransaction
//    public void save(Price msg) throws Exception {
//        cubePriceEmitter.send(new Price(msg.value * 3));
//
//        if (msg.value % 2 == 0) {
//            throw new Exception("eh par");
//        }
//
//    }

    @Incoming("prices")

    public CompletionStage<Void> consumeMessage(Message<Price> price) throws Exception {
        Message<Price> sendMsg = null;
        try {

            Price price1 = new Price(price.getPayload().value * 3);
            Message<Price> sendMsg = Message.of(price1);
            cubePriceEmitter.send(Message.of(price1));
            if (price.getPayload().value % 2 == 0) {
                throw new Exception("eh par");
            }
            return price.ack();
        } catch (Exception e) {
            if (sendMsg != null) {
                sendMsg.nack(e);
            }
            return price.nack(e);
        }
    }
}
