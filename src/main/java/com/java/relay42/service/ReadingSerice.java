package com.java.relay42.service;

import com.java.relay42.config.StationDetailsMap;
import com.java.relay42.dto.ReadingsDTO;
import com.java.relay42.entity.ReadingValue;
import com.java.relay42.repository.ReadingsRepository;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

/**
 * Service to fetch the values from  ReadingsRepository
 */
@Service
public class ReadingSerice {
    private static final Logger log = LoggerFactory.getLogger(ReadingSerice.class);
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
        log.debug("Reading request from user to fetch average value ---{}",readings);
        return readingsRepository.
                findAverageByKey(
                        readings.getFromTime(), readings.getToTime(), getStationId(readings)).doubleValue();
    }

    /**
     * Method to fetch Maximum of the readings in given time range
     * @param readings Readings DTO with Time Range and Type
     * @return Max value
     */
    public BigInteger maxOfReadings(ReadingsDTO readings) {
        log.debug("Reading request from user to fetch max value ---{}",readings);
        return readingsRepository.
                findMaxValueByKey(
                        readings.getFromTime(), readings.getToTime(), getStationId(readings));
    }
    /**
     * Method to fetch Minimum of the readings in given time range
     * @param readings Readings DTO with Time Range and Type
     * @return Min value
     */
    public BigInteger minOfReadings(ReadingsDTO readings) {
        log.debug("Reading request from user to fetch min value ---{}",readings);
        return readingsRepository.
                findMinValueByKey(
                        readings.getFromTime(), readings.getToTime(), getStationId(readings));
    }
    /**
     * Method to fetch Median of the readings in given time range
     * @param readings Readings DTO with Time Range and Type
     * @return Median value
     */
    public Double medianOfReadings(ReadingsDTO readings) {
        log.debug("Reading request from user to fetch median value ---{}",readings);
        List<ReadingValue> values = readingsRepository.
                findAllReadingValueByKey(
                        readings.getFromTime(), readings.getToTime(), getStationId(readings));
        double[] dbl = values.stream()
                .mapToDouble(e -> e.getReading().doubleValue())
                .toArray();

        // create an object of Median class
        Median median = new Median();
        //Used Apache library to calculate the median
        return median.evaluate(dbl);
    }

    /**
     * If StationId is not passed by the user , stationid will be fetched from the
     * StationDetails map based on the producer type
     * @param readings Readings json input passed by user.
     * @return stationID
     */
    private UUID getStationId(ReadingsDTO readings) {
        return null != readings.getSensorId() ? readings.getSensorId() : stationDetailsMap.getStationIdMap().get(readings.getProducerType());
    }

}
