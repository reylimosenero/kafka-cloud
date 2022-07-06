package com.example.demo;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
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
class Producer {

    private final KafkaTemplate<Integer, String> template;

    Faker faker;

    @EventListener(ApplicationStartedEvent.class)
    public void generate() {
        faker = Faker.instance();
        Flux<Long> interval = Flux.interval(Duration.ofMillis(1_000));
        Flux<String> objectFlux = Flux.fromStream(Stream.generate(new Supplier<String>() {
            @Override
            public String get() {
                return faker.hobbit().quote();
            }
        }));

        Flux.zip(interval, objectFlux).map(new Function<Tuple2<Long, String>, Object>() {
            @Override
            public Object apply(final Tuple2<Long, String> it) {
                return template.send("mini-proj-topic", faker.random().nextInt(42), it.getT2());
            }
        }).blockLast();

    }

}
