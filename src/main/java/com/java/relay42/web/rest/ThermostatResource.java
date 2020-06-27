package com.java.relay42.web.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

/**
 * REST controller for managing Simulation of data for ThermoStat Readings.
 */
@RestController
@RequestMapping("/publisher")
public class ThermostatResource {
    private Random rand = SecureRandom.getInstanceStrong();

    public ThermostatResource() throws NoSuchAlgorithmException {
        //Do nothing
    }

    /**
     * {@code GET  /sensors/:id} : no args.
     *
     * @return the {@link Flux<Integer>} with status {@code 200 (OK)} and with body the Readings,
     * Acts as a producer for simulating data.
     */
    @GetMapping(value = "/temperatures", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ConditionalOnProperty(name = "simulate.sensor-data", havingValue = "true")
    public Flux<Integer> getTemperature() {
        int low = 0;
        int high = 50;
        return Flux.fromStream(Stream.generate(() -> this.rand.nextInt(high - low) + low))
                .delayElements(Duration.ofSeconds(1));
    }
}
