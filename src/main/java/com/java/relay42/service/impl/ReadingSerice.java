package com.java.relay42.service.impl;

import com.java.relay42.config.StationDetailsMap;
import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.entity.ReadingValue;
import com.java.relay42.repository.ReadingsRepository;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ReadingSerice {
    @Autowired
    private ReadingsRepository readingsRepository;

    @Autowired
    private StationDetailsMap stationDetailsMap;

    public Double averageOfReadings(ReadingsDTO readings) {
        return readingsRepository.
                findAverageByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType())).doubleValue();
    }

    public BigInteger maxOfReadings(ReadingsDTO readings) {
        return readingsRepository.
                findMaxValueByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType()));
    }

    public BigInteger minOfReadings(ReadingsDTO readings) {
        return readingsRepository.
                findMinValueByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType()));
    }

    public Double medianOfReadings(ReadingsDTO readings) {
        List<ReadingValue> values = readingsRepository.
                findAllReadingValueByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType()));
        double[] dbl = values.stream()
                .mapToDouble(e -> e.getReading().doubleValue())
                .toArray();

        // create an object of Median class
        Median median = new Median();
        // calculate median
        //double evaluate;
        return median.evaluate(dbl);
    }
}
