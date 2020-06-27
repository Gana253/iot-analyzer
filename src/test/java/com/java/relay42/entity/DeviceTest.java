package com.java.relay42.entity;

import com.java.relay42.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceTest {

    @Test
    public void equalsVerifier() throws Exception {
        Device device1 = new Device();
        device1.setId(UUID.randomUUID());
        Device device2 = new Device();
        device2.setId(device1.getId());
        assertThat(device1).isEqualTo(device2);
        device2.setId(UUID.randomUUID());
        assertThat(device1).isNotEqualTo(device2);
        device1.setId(null);
        assertThat(device1).isNotEqualTo(device2);
    }
}
