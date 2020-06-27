package com.java.relay42.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SensorTest {

    @Test
    public void equalsVerifier() throws Exception {
        Sensor sensor1 = new Sensor();
        sensor1.setId(UUID.randomUUID());
        Sensor sensor2 = new Sensor();
        sensor2.setId(sensor1.getId());
        assertThat(sensor1).isEqualTo(sensor2);
        sensor2.setId(UUID.randomUUID());
        assertThat(sensor1).isNotEqualTo(sensor2);
        sensor1.setId(null);
        assertThat(sensor1).isNotEqualTo(sensor2);
    }
}
