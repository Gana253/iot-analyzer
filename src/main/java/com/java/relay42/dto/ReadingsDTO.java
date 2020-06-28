package com.java.relay42.dto;

import com.java.relay42.config.ProducerEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * DTO for fetching the AVG,MAX,MIN,MEDIAN value of the sensor
 */
@Getter
@Setter
@NoArgsConstructor
public class ReadingsDTO implements Serializable {
    private static final long serialVersionUID = 6641134412914065796L;
    private Instant fromTime;
    private Instant toTime;
    private ProducerEnum producerType;
    private UUID sensorId;
}
