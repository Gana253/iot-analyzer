package com.java.relay42.config;

import com.java.relay42.service.IotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class IotConfiguration {
    Logger logger = LoggerFactory.getLogger(IotConfiguration.class);

    @Autowired
    private IotService iotService;

    @Bean
    WebClient getWebClient() {
        return WebClient.create("http://localhost:5678/publisher");
    }

    @Bean
    @ConditionalOnProperty(name="simulate.sensor-data", havingValue="true")
    CommandLineRunner thermostatConsumer(WebClient client) {
        return args -> client.get()
                .uri("/temperatures")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Integer.class)
                .subscribe(s -> iotService.buildReadingObjForPersist(s, ProducerEnum.THERMOSTAT));
    }

    @Bean
    @ConditionalOnProperty(name="simulate.sensor-data", havingValue="true")
    CommandLineRunner hearRateConsumer(WebClient client) {
        return args -> client.get()
                .uri("/heartrate")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Integer.class)
                .subscribe(s -> iotService.buildReadingObjForPersist(s, ProducerEnum.HEARTRATEMETER));
    }

    @Bean
    @ConditionalOnProperty(name="simulate.sensor-data", havingValue="true")
    CommandLineRunner fuelReadingConsumer(WebClient client) {
        return args -> client.get()
                .uri("/fuelReading")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Integer.class)
                .subscribe(s -> iotService.buildReadingObjForPersist(s, ProducerEnum.FUELREADER));
    }

  /*  @Bean
    public FormattingConversionService conversionService() {
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        registrar.registerFormatters(conversionService);

        return conversionService;
    }*/
}
