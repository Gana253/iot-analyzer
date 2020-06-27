package com.java.relay42.web.rest;


import com.datastax.driver.core.utils.UUIDs;
import com.java.relay42.IotAnalyzerApplication;
import com.java.relay42.entity.Sensor;
import com.java.relay42.repository.SensorRepository;
import com.java.relay42.web.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SensorResource} REST controller.
 */
@SpringBootTest(classes = IotAnalyzerApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class SensorResourceIT {

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";


    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createEntity() {
        Sensor sensor = new Sensor();
        sensor.setSensorName(DEFAULT_DEVICE_NAME);
        sensor.setClientName(DEFAULT_CLIENT_NAME);
        sensor.setLocation(DEFAULT_LOCATION);
        sensor.setUnit(DEFAULT_UNIT);
        sensor.setStationName("AMS");
        sensor.setCreatedBy("SYSTEM");
        return sensor;
    }

    @BeforeEach
    public void initTest() {
        sensorRepository.deleteAll();
        sensor = createEntity();
    }

    @Test
    public void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();
        // Create the Sensor
        restSensorMockMvc.perform(post("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(sensor)))
                .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getSensorName()).isEqualTo(DEFAULT_DEVICE_NAME);
        assertThat(testSensor.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testSensor.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSensor.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    public void createSensorWithExistingId() throws Exception {


        // Create the Sensor with an existing ID
        sensor.setId(UUID.randomUUID());

        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc.perform(post("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(sensor)))
                .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllSensors() throws Exception {
        // Initialize the database
        sensor.setId(UUID.randomUUID());
        sensorRepository.save(sensor);

        // Get all the sensorList
        restSensorMockMvc.perform(get("/api/sensors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().toString())))
                .andExpect(jsonPath("$.[*].sensorName").value(hasItem(DEFAULT_DEVICE_NAME)))
                .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
                .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)));
    }

    @Test
    public void getSensor() throws Exception {
        // Initialize the database
        sensor.setId(UUID.randomUUID());
        sensorRepository.save(sensor);

        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", sensor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(sensor.getId().toString()))
                .andExpect(jsonPath("$.sensorName").value(DEFAULT_DEVICE_NAME))
                .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
                .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
                .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT));
    }

    @Test
    public void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateSensor() throws Exception {
        // Initialize the database
        sensor.setId(UUID.randomUUID());
        sensorRepository.save(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).get();

        updatedSensor.setSensorName(UPDATED_DEVICE_NAME);
        updatedSensor.setClientName(UPDATED_CLIENT_NAME);
        updatedSensor.setLocation(UPDATED_LOCATION);
        updatedSensor.setUnit(UPDATED_UNIT);
        updatedSensor.setStationName("AMS");
        updatedSensor.setStationId(UUIDs.timeBased());
        updatedSensor.setCreatedBy("SYSTEM");

        restSensorMockMvc.perform(put("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedSensor)))
                .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getSensorName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testSensor.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testSensor.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSensor.getUnit()).isEqualTo(UPDATED_UNIT);
    }


    @Test
    public void updateNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc.perform(put("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(sensor)))
                .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }


    @Test
    public void deleteSensor() throws Exception {
        // Initialize the database
        sensor.setId(UUID.randomUUID());
        sensorRepository.save(sensor);

        int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Delete the sensor
        restSensorMockMvc.perform(delete("/api/sensors/{id}", sensor.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
