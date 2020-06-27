package com.java.relay42.web.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

/**
 * REST controller for managing Simulation of data for HeartRateMeter.
 */
@RestController
@RequestMapping("/publisher")
public class HeartRateMeterResource {
    /**
     * {@code GET  /devices/:id} : no args.
     *
     * @return the {@link Flux<Integer>} with status {@code 200 (OK)} and with body the Readings,
     * Acts as a producer for simulating data.
     */
    @GetMapping(value = "/heartrate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ConditionalOnProperty(name = "simulate.sensor-data", havingValue = "true")
    public Flux<Integer> getHeartRate() {
        Random r = new Random();
        int low = 1;
        int high = 250;
        return Flux.fromStream(Stream.generate(() -> r.nextInt(high - low) + low))
                .delayElements(Duration.ofSeconds(1));
    }


}
