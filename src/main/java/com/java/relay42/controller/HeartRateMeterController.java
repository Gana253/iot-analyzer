package com.java.relay42.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

@RestController
@RequestMapping("/publisher")
public class HeartRateMeterController {

    @GetMapping(value = "/heartrate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getHeartRate() {
        Random r = new Random();
        int low = 1;
        int high = 250;
        return Flux.fromStream(Stream.generate(() -> r.nextInt(high - low) + low))
                .delayElements(Duration.ofSeconds(1));
    }


}
