package org.acme.panache;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.kafka.commit.CheckpointMetadata;
import jakarta.persistence.PersistenceException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.hibernate.exception.ConstraintViolationException;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class PriceStorage {

    Set<Integer> numbers = new HashSet<>();


    @Incoming("prices")
    @Blocking
    @Transactional(transactionManager = "kafkaTransactionManager", propagation = Propagation.REQUIRED)
    @Retry(delay = 10000, maxRetries = 10000)
    public CompletionStage<Void> store(ConsumerRecord<Object,Integer> record) {
        try {

            System.out.printf("VALOR: %d\n", record.getPayload());
            Price price = new Price();
            price.value = record.getPayload();

            price.persist();

            return record.ack();
        } catch (ConstraintViolationException exception) {
            return record.nack(exception);
        }

    }

}
