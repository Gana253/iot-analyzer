package com.java.relay42.web.rest;

import com.java.relay42.IotAnalyzerApplication;
import com.java.relay42.config.ProducerEnum;
import com.java.relay42.dto.JwtRequest;
import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.repository.ReadingsRepository;
import com.java.relay42.service.IotService;
import com.java.relay42.web.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Integration Test for the {@link IotAnalyzerResource} REST controller. With Security
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = IotAnalyzerApplication.class)
public class IotResourceWithSecurityIT {
    @Autowired
    private MockMvc iotMockMVC;

    @Autowired
    private IotService iotService;

    @Autowired
    private ReadingsRepository readingsRepository;


    public void initReadings(ProducerEnum enumVal){
        readingsRepository.deleteAll();
        iotService.buildReadingObjForPersist(5,enumVal);
        iotService.buildReadingObjForPersist(10,enumVal);
        iotService.buildReadingObjForPersist(15,enumVal);
        iotService.buildReadingObjForPersist(20,enumVal);
        iotService.buildReadingObjForPersist(25,enumVal);
    }

    @Test
    public void findAvgForFuelReaderWithAuthentication() throws Exception {

        JwtRequest request = new JwtRequest();
        request.setUsername("admin");
        request.setPassword("admin");


        MvcResult resultJwtToken = iotMockMVC
                .perform(post("/authenticate")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isOk())
                .andReturn();
        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/avg")
                        .header("Authorization" , "Bearer "+resultJwtToken.getResponse().getContentAsString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(Double.parseDouble(result.getResponse().getContentAsString())).isEqualTo(15.0);
    }

    @Test
    public void findAvgForFuelReaderWithoutAuthentication() throws Exception {


        ReadingsDTO readings = new ReadingsDTO();
        readings.setFromTime(Instant.now());
        initReadings(ProducerEnum.FUELREADER);
        readings.setToTime(Instant.now());
        readings.setProducerType(ProducerEnum.FUELREADER);


        MvcResult result = iotMockMVC
                .perform(post("/relay42/iot/avg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(readings)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
