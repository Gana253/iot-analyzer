package com.java.relay42.service;

import com.java.relay42.config.StationDetailsMap;
import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.entity.ReadingValue;
import com.java.relay42.repository.ReadingsRepository;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * Service to fetch the values from  ReadingsRepository
 */
@Service
public class ReadingSerice {
    @Autowired
    private ReadingsRepository readingsRepository;

    @Autowired
    private StationDetailsMap stationDetailsMap;

    /**
     * Method to fetch average of the readings in given time range
     * @param readings Readings DTO with Time Range and Type
     * @return Average value
     */
    public Double averageOfReadings(ReadingsDTO readings) {
        return readingsRepository.
                findAverageByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType())).doubleValue();
    }
    /**
     * Method to fetch Maximum of the readings in given time range
     * @param readings Readings DTO with Time Range and Type
     * @return Max value
     */
    public BigInteger maxOfReadings(ReadingsDTO readings) {
        return readingsRepository.
                findMaxValueByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType()));
    }
    /**
     * Method to fetch Minimum of the readings in given time range
     * @param readings Readings DTO with Time Range and Type
     * @return Min value
     */
    public BigInteger minOfReadings(ReadingsDTO readings) {
        return readingsRepository.
                findMinValueByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType()));
    }
    /**
     * Method to fetch Median of the readings in given time range
     * @param readings Readings DTO with Time Range and Type
     * @return Median value
     */
    public Double medianOfReadings(ReadingsDTO readings) {
        List<ReadingValue> values = readingsRepository.
                findAllReadingValueByKey(
                        readings.getFromTime(), readings.getToTime(), stationDetailsMap.getStationIdMap().get(readings.getProducerType()));
        double[] dbl = values.stream()
                .mapToDouble(e -> e.getReading().doubleValue())
                .toArray();

        // create an object of Median class
        Median median = new Median();
        //Used Apache library to calculate the median
        return median.evaluate(dbl);
    }
}
