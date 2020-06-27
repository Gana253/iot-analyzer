package com.java.relay42.web.rest;

import com.java.relay42.IotAnalyzerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test for the {@link HeartRateMeterResource} REST controller.
 */
@TestPropertySource(properties = "simulate.sensor-data=true")
@AutoConfigureMockMvc
@SpringBootTest(classes = IotAnalyzerApplication.class)
@WithMockUser
public class HeartRateMeterResourceIT {
    @Autowired
    private MockMvc restHeartRateMockMvc;

    @Test
    public void testFilterReadingUri() throws Exception {
        restHeartRateMockMvc
                .perform(get("/publisher/heartrate"))
                .andExpect(status().isOk());
    }
}
