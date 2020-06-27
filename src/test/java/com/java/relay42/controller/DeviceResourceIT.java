package com.java.relay42.controller;



import com.datastax.driver.core.utils.UUIDs;
import com.java.relay42.IotAnalyzerApplication;
import com.java.relay42.entity.Device;
import com.java.relay42.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@SpringBootTest(classes = IotAnalyzerApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class DeviceResourceIT  {

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity() {
        Device device = new Device();
        device.setDeviceName(DEFAULT_DEVICE_NAME);
        device.setClientName(DEFAULT_CLIENT_NAME);
        device.setLocation(DEFAULT_LOCATION);
        device.setUnit(DEFAULT_UNIT);
        device.setStationName("AMS");
        device.setCreatedBy("SYSTEM");
        return device;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity() {

        Device device = new Device();
        device.setDeviceName(UPDATED_DEVICE_NAME);
        device.setClientName(UPDATED_CLIENT_NAME);
        device.setLocation(UPDATED_LOCATION);
        device.setUnit(UPDATED_UNIT);
        device.setStationName("AMS");
        device.setCreatedBy("SYSTEM");
        return device;
    }

    @BeforeEach
    public void initTest() {
        deviceRepository.deleteAll();
        device = createEntity();
    }

    @Test
    public void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();
        // Create the Device
        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
        assertThat(testDevice.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testDevice.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testDevice.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    public void createDeviceWithExistingId() throws Exception {


        // Create the Device with an existing ID
        device.setId(UUID.randomUUID());

        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllDevices() throws Exception {
        // Initialize the database
        device.setId(UUID.randomUUID());
        deviceRepository.save(device);

        // Get all the deviceList
        restDeviceMockMvc.perform(get("/api/devices"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME)))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)));
    }
    
    @Test
    public void getDevice() throws Exception {
        // Initialize the database
        device.setId(UUID.randomUUID());
        deviceRepository.save(device);

        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().toString()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT));
    }
    @Test
    public void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDevice() throws Exception {
        // Initialize the database
        device.setId(UUID.randomUUID());
        deviceRepository.save(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();

        updatedDevice.setDeviceName(UPDATED_DEVICE_NAME);
        updatedDevice.setClientName(UPDATED_CLIENT_NAME);
        updatedDevice.setLocation(UPDATED_LOCATION);
        updatedDevice.setUnit(UPDATED_UNIT);
        updatedDevice.setStationName("AMS");
        updatedDevice.setStationId(UUIDs.timeBased());
        updatedDevice.setCreatedBy("SYSTEM");

        restDeviceMockMvc.perform(put("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDevice)))
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testDevice.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testDevice.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testDevice.getUnit()).isEqualTo(UPDATED_UNIT);
    }


    @Test
    public void updateNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc.perform(put("/api/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }


    @Test
    public void deleteDevice() throws Exception {
        // Initialize the database
        device.setId(UUID.randomUUID());
        deviceRepository.save(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Delete the device
        restDeviceMockMvc.perform(delete("/api/devices/{id}", device.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
