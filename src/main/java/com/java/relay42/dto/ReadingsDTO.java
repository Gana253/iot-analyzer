package com.java.relay42.dto;

import com.java.relay42.config.ProducerEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingsDTO implements Serializable {
    private static final long serialVersionUID = 6641134412914065796L;
    private Instant fromTime;
    private Instant toTime;
    private ProducerEnum producerType;
}
