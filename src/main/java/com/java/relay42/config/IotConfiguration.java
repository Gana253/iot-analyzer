package com.java.relay42.config;


import com.java.relay42.service.IotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Class to consume the simulation data for the sensors.
 */
@Configuration
public class IotConfiguration {
    Logger logger = LoggerFactory.getLogger(IotConfiguration.class);

    @Autowired
    private IotService iotService;

    /**
     * Web client to fetch the data from the publisher.
     *
     * @return WebClient
     */
    @Bean
    WebClient getWebClient() {
        return WebClient.create("http://localhost:5678/publisher");
    }

    /**
     * Consumer for the thermostat readings . It will accept the data from the producer and persist it in Readings table
     *
     * @param client to accept the data
     * @return {@link CommandLineRunner}
     */
    @Bean
    @ConditionalOnProperty(name = "simulate.sensor-data", havingValue = "true")
    CommandLineRunner thermostatConsumer(WebClient client) {
        return args -> client.get()
                .uri("/temperatures")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Integer.class)
                .subscribe(s -> iotService.buildReadingObjForPersist(s, ProducerEnum.THERMOSTAT));
    }

    /**
     * Consumer for the HeartRateMeter readings . It will accept the data from the producer and persist it in Readings table
     *
     * @param client to accept the data
     * @return {@link CommandLineRunner}
     */
    @Bean
    @ConditionalOnProperty(name = "simulate.sensor-data", havingValue = "true")
    CommandLineRunner hearRateConsumer(WebClient client) {
        return args -> client.get()
                .uri("/heartrate")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Integer.class)
                .subscribe(s -> iotService.buildReadingObjForPersist(s, ProducerEnum.HEARTRATEMETER));
    }

    /**
     * Consumer for the FuelMeter readings . It will accept the data from the producer and persist it in Readings table
     *
     * @param client to accept the data
     * @return {@link CommandLineRunner}
     */
    @Bean
    @ConditionalOnProperty(name = "simulate.sensor-data", havingValue = "true")
    CommandLineRunner fuelReadingConsumer(WebClient client) {
        return args -> client.get()
                .uri("/fuelReading")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Integer.class)
                .subscribe(s -> iotService.buildReadingObjForPersist(s, ProducerEnum.FUELREADER));
    }

}
