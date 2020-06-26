package com.java.relay42.controller;

import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.service.impl.ReadingSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigInteger;

@RestController
@RequestMapping("/relay42/iot")
public class IotController {
    @Autowired
    private ReadingSerice readingSerice;

    @PostMapping("/avg")
    public Double averageReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        //readingSerice.averageOfReadings(readings);
        return readingSerice.averageOfReadings(readings);
    }

    @PostMapping("/max")
    public BigInteger maxReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        //readingSerice.averageOfReadings(readings);
        return readingSerice.maxOfReadings(readings);
    }

    @PostMapping("/min")
    public BigInteger minReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        //readingSerice.averageOfReadings(readings);
        return readingSerice.minOfReadings(readings);
    }

    @PostMapping("/median")
    public Double medianReadingValue(@Valid @RequestBody ReadingsDTO readings) {
        //readingSerice.averageOfReadings(readings);
        return readingSerice.medianOfReadings(readings);
    }

}
