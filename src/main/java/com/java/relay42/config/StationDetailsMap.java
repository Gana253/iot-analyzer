package com.java.relay42.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.UUID;

/**
 * Map to store the station details and their Id
 */
@Configuration("stationDetailsMap")
@Scope(WebApplicationContext.SCOPE_APPLICATION)
@Getter
@Setter
public class StationDetailsMap {
    private Map<ProducerEnum, UUID> stationIdMap;

}
