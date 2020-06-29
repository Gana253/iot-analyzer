package com.java.relay42.web.rest;

import com.java.relay42.IotAnalyzerApplication;
import com.java.relay42.config.ProducerEnum;
import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.repository.ReadingsRepository;
import com.java.relay42.service.IotService;
import com.java.relay42.web.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Integration Test for the {@link IotAnalyzerResource} REST controller.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = IotAnalyzerApplication.class)
@WithMockUser
public class IotResourceIT {
    public static final double DOUBLE_15 = 15.0;
    public static final double DOUBLE_5 = 5.0;
    public static final double DOUBLE_25 = 25.0;
    @Autowired
    private MockMvc iotMockMVC;

    @Autowired
    private IotService iotService;

    @Autowired
    private ReadingsRepository readingsRepository;


    public void initReadings(ProducerEnum enumVal) {

        readingsRepository.deleteAll();
        iotService.buildReadingObjForPersist(5, enumVal);
        iotService.buildReadingObjForPersist(10, enumVal);
        iotService.buildReadingObjForPersist(15, enumVal);
        iotService.buildReadingObjForPersist(20, enumVal);
        iotService.buildReadingObjForPersist(25, enumVal);
    }

    @Test
    public void findAvgForFuelReader() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/avg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_15);
    }

    @Test
    public void findMaxForFuelReader() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/max")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_25);
    }

    @Test
    public void findMinForFuelReader() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_5);
    }

    @Test
    public void findMedianForFuelReader() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/median")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_15);
    }

    @Test
    public void findAvgForHeartRate() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.HEARTRATEMETER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.HEARTRATEMETER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/avg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_15);
    }

    @Test
    public void findMaxForHeartRate() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.HEARTRATEMETER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.HEARTRATEMETER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/max")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_25);
    }

    @Test
    public void findMinForHeartRate() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.HEARTRATEMETER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.HEARTRATEMETER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_5);
    }

    @Test
    public void findMedianForHeartRate() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.HEARTRATEMETER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.HEARTRATEMETER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/median")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_15);
    }

    @Test
    public void findAvgForThermoStat() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.THERMOSTAT);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.THERMOSTAT);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/avg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_15);
    }

    @Test
    public void findMaxForThermoStat() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.THERMOSTAT);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.THERMOSTAT);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/max")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_25);
    }

    @Test
    public void findMinForThermoStat() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.THERMOSTAT);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.THERMOSTAT);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_5);
    }

    @Test
    public void findMedianForThermoStat() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.THERMOSTAT);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.THERMOSTAT);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/median")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(DOUBLE_15);
    }


    @Test
    public void findMaxForFuelReaderWithInvalidTimeRange() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


       iotMockMVC
                .perform(post("/relay42/iot/max")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.title").value("Invalid Time Range passed"))
               .andExpect(jsonPath("$.errorKey").value("End time cannot be before the Start time"));
    }



    @Test
    public void findMaxWithNoProducerTypeAndStationId() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());


        iotMockMVC
                .perform(post("/relay42/iot/max")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid input passed"))
                .andExpect(jsonPath("$.errorKey").value("SensorId and ProducerType cannot be empty"));
    }

    @Test
    public void findAvgForFuelReaderWithInvalidTimeRange() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        iotMockMVC
                .perform(post("/relay42/iot/avg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Time Range passed"))
                .andExpect(jsonPath("$.errorKey").value("End time cannot be before the Start time"));
    }



    @Test
    public void findAvgWithNoProducerTypeAndStationId() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());


        iotMockMVC
                .perform(post("/relay42/iot/avg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid input passed"))
                .andExpect(jsonPath("$.errorKey").value("SensorId and ProducerType cannot be empty"));
    }

    @Test
    public void findMinForFuelReaderWithInvalidTimeRange() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        iotMockMVC
                .perform(post("/relay42/iot/min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Time Range passed"))
                .andExpect(jsonPath("$.errorKey").value("End time cannot be before the Start time"));
    }



    @Test
    public void findMinWithNoProducerTypeAndStationId() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());


        iotMockMVC
                .perform(post("/relay42/iot/min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid input passed"))
                .andExpect(jsonPath("$.errorKey").value("SensorId and ProducerType cannot be empty"));
    }

    @Test
    public void findMedianForFuelReaderWithInvalidTimeRange() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        iotMockMVC
                .perform(post("/relay42/iot/median")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Time Range passed"))
                .andExpect(jsonPath("$.errorKey").value("End time cannot be before the Start time"));
    }



    @Test
    public void findMedianWithNoProducerTypeAndStationId() throws Exception {
        ReadingsDTO readings = new ReadingsDTO();
        readings.setToTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setFromTime(Instant.now());


        iotMockMVC
                .perform(post("/relay42/iot/median")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid input passed"))
                .andExpect(jsonPath("$.errorKey").value("SensorId and ProducerType cannot be empty"));
    }
}
