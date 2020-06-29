package com.java.relay42.web.rest;

import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.service.ReadingSerice;
import com.java.relay42.util.IotValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigInteger;

/**
 * REST controller for managing request to get the Avg,Min,Max,Median of readings
 */
@RestController
@RequestMapping("/relay42/iot")
public class IotAnalyzerResource {
    @Autowired
    private ReadingSerice readingSerice;

    /**
     * {@code POST  /Readings} : Fetch Average value for the given time and sensor type.
     * @param readings Json values with time range,sensor type
     * @return  Average value
     */
    @PostMapping("/avg")
    public Double averageReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        IotValidatorUtils.validateReadingsEndpointInput(readings);
        return readingSerice.averageOfReadings(readings);
    }
    /**
     * {@code POST  /Readings} : Fetch Max value for the given time and sensor type.
     * @param readings Json values with time range,sensor type
     * @return  Max value
     */
    @PostMapping("/max")
    public BigInteger maxReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        IotValidatorUtils.validateReadingsEndpointInput(readings);
        return readingSerice.maxOfReadings(readings);
    }
    /**
     * {@code POST  /Readings} : Fetch Min value for the given time and sensor type.
     * @param readings Json values with time range,sensor type
     * @return  Min value
     */
    @PostMapping("/min")
    public BigInteger minReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        IotValidatorUtils.validateReadingsEndpointInput(readings);
        return readingSerice.minOfReadings(readings);
    }
    /**
     * {@code POST  /Readings} : Fetch Median value for the given time and sensor type.
     * @param readings Json values with time range,sensor type
     * @return  Median value
     */
    @PostMapping("/median")
    public Double medianReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        IotValidatorUtils.validateReadingsEndpointInput(readings);
        return readingSerice.medianOfReadings(readings);
    }

}
