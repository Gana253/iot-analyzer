package com.java.relay42.controller;

import com.java.relay42.dto.Greeting;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@RestController
@RequestMapping
public class GreetReactiveController {
    @GetMapping("/greetings")
    public Publisher<Greeting> greetingPublisher() {
        Flux<Greeting> greetingFlux = Flux.<Greeting>generate(sink -> sink.next(new Greeting("Hello"))).take(50);
        return greetingFlux;
    }

    @GetMapping(value = "/greetings/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Greeting> events() {
        Flux<Greeting> greetingFlux = Flux.fromStream(Stream.generate(() -> new Greeting("Hello @" + Instant.now().toString())));
        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(greetingFlux, durationFlux).map(Tuple2::getT1);
    }
}