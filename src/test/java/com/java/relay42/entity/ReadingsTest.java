package com.java.relay42.entity;

import com.datastax.driver.core.utils.UUIDs;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadingsTest {
    @Test
    public void equalsVerifier() throws Exception {
        Readings readings1 = new Readings();
        readings1.setId(UUID.randomUUID());
        Readings readings2 = new Readings();
        readings2.setId(readings1.getId());
        assertThat(readings1).isEqualTo(readings2);
        readings2.setId(UUID.randomUUID());
        assertThat(readings1).isNotEqualTo(readings2);
        readings1.setId(null);
        assertThat(readings1).isNotEqualTo(readings2);
        readings1.setKey(new ReadingsPrimaryKey(UUIDs.timeBased()));
        readings2.setKey(readings1.getKey());
        assertThat(readings1).isNotEqualTo(readings2);
    }
}
