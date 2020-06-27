package com.java.relay42;


import com.java.relay42.constants.IotConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IotAnalyzerApplicationTests {

    @Test
    void contextLoads() {
        assertThat("ROLE_ADMIN").isEqualTo(IotConstants.ADMIN);
    }

}
