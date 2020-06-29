package com.java.relay42.util;

import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.exception.BadRequestAlertException;
import org.springframework.util.StringUtils;

public final class IotValidatorUtils {

    public static void validateReadingsEndpointInput(ReadingsDTO readingsDTO) {
        if (StringUtils.isEmpty(readingsDTO.getSensorId()) && StringUtils.isEmpty(readingsDTO.getProducerType())) {
            throw new BadRequestAlertException("Invalid input passed", "Iot Analyzer", "SensorId and ProducerType cannot be empty");
        }
        if (readingsDTO.getToTime().isBefore(readingsDTO.getFromTime())) {
            throw new BadRequestAlertException("Invalid Time Range passed", "Iot Analyzer", "End time cannot be before the Start time");
        }
    }
}