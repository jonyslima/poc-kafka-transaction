package org.acme.panache;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.kafka.KafkaRecordBatch;
import io.smallrye.reactive.messaging.kafka.transactions.KafkaTransactions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.*;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class PriceStorage {

//    Set<Integer> numbers = new HashSet<>();
//
//
//    @Incoming("prices")
//    @Blocking
//    @Transactional
//    @Retry(delay = 10000, maxRetries = 10000)
//    public CompletionStage<Void> store(Message<Integer> record) {
////        try {
////            System.out.printf("VALOR: %d\n", record.getPayload());
////            Price price = new Price();
////            price.value = record.getPayload();
////
////            price.persist();
//
//            return record.ack();
////        } catch (ConstraintViolationException exception) {
////            return record.nack(exception);
////        }
//
//    }


    @Channel("cube-price")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 500)
    KafkaTransactions<Price> txProducer;


    @Inject
    Service service;

    @Incoming("prices")
    @Retry(delay = 10, maxRetries = 10, delayUnit = ChronoUnit.SECONDS)
    @Blocking
    @Transactional
    public Uni<Void> emitInTransaction(KafkaRecordBatch<String, Price> batch) {
        System.out.println(batch.getClass());
        return txProducer.withTransactionAndAck(batch, emitter -> {
            for (KafkaRecord<String, Price> record : batch) {
                System.out.printf("VALOR: %d%n", record.getPayload().value);
                if (record.getPayload().value % 2 == 0) {
                    service.process(record.getPayload());
                    Price price = new Price(record.getPayload());
                    price.value *= 3;
                    //emitter.send(KafkaRecord.of(record.getKey(), price));
                }
            }
            return Uni.createFrom().voidItem();
        });
    }


}
