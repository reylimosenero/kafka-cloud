package com.example.demo;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
class Consumer {

    private final KafkaTemplate<Integer, String> template;

    Faker faker;

    @KafkaListener(topics = {"mini-proj-topic"},groupId = "spring-boot-kafka")
    public void consume(String quote) {
        System.out.println(" received = "+ quote);

    }

}
