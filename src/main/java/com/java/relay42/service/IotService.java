package com.java.relay42.service;

import com.datastax.driver.core.utils.UUIDs;
import com.java.relay42.config.ProducerEnum;
import com.java.relay42.config.StationDetailsMap;
import com.java.relay42.dto.UserDTO;
import com.java.relay42.entity.Readings;
import com.java.relay42.entity.ReadingsPrimaryKey;
import com.java.relay42.entity.Sensor;
import com.java.relay42.repository.ReadingsRepository;
import com.java.relay42.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

/**
 * Serivce class for the application. To load data and persis the consumer reading request.
 */
@Service
public class IotService {

    public static final String COMMA = ";";

    private static final Logger log = LoggerFactory.getLogger(IotService.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private ReadingsRepository readingsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StationDetailsMap stationDetailsMap;

    @Value("${inputfile.user-name}")
    private String userFilePath;

    @Value("${inputfile.sensor-name}")
    private String sensorFilePath;

    /**
     * Load User data and Sensor data on startup
     *
     * @throws IOException when CSV file is not found
     */
    public void loadUsersAndSensorData() throws IOException {
        //Load User from the user.csv file and persist in table
        saveUserData();
        //Load sensor from the sensorlist.csv file and persist in table
        loadSensorData();
    }

    /**
     * Assists consumer to save the reading value received.
     *
     * @param reading     Reading value and Station Type
     * @param stationType Thermostat/FuelReader/HearRateMachine
     */
    public void buildReadingObjForPersist(Integer reading, ProducerEnum stationType) {
        Readings readingObj = new Readings();
        readingObj.setId(UUIDs.timeBased());
        readingObj.setReading(Double.valueOf(reading));

        readingObj.setKey(new ReadingsPrimaryKey(stationDetailsMap.getStationIdMap().get(stationType)));
        log.debug("Reading object to be store --{}", readingObj);
        readingsRepository.save(readingObj);

    }

    private void loadSensorData() throws IOException {
        Resource sensorResource = null;

        try {
            sensorResource = resourceLoader.getResource(sensorFilePath);
            File file = sensorResource.getFile();
            List<String> userData = Files.readAllLines(file.toPath());

            try (Stream<String> stream = userData.stream()) {
                stream.skip(1).map(line -> line.split(COMMA)).forEach(data -> {
                    Sensor sensor = new Sensor();
                    sensor.setId(UUIDs.timeBased());
                    sensor.setStationId(UUIDs.timeBased());
                    sensor.setCreatedBy("SYSTEM");
                    for (int i = 0; i < data.length; i++) {
                        switch (i) {
                            case 0:
                                sensor.setSensorName(data[i].toLowerCase());
                                break;
                            case 1:
                                sensor.setClientName(data[i].toLowerCase());
                                break;
                            case 2:
                                sensor.setStationName(data[i].toLowerCase());
                                break;
                            case 3:
                                sensor.setLocation(data[i].toLowerCase());
                                break;
                            default:
                                sensor.setUnit(data[i].toLowerCase());
                                break;
                        }
                    }
                    //Track the station details data with station id
                    setStationDetails(sensor);
                    log.debug("Sensor object to be stored --{}", sensor);
                    sensorRepository.save(sensor);
                });
            }

        } catch (IOException e) {
            log.error("Exception while parsing sensor data--{}", e.getMessage());
        } finally {
            if (null != sensorResource) sensorResource.readableChannel().close();
        }
    }

    private void setStationDetails(Sensor sensor) {
        log.info("updating the tracker object with {}", sensor.getStationName());
        EnumMap<ProducerEnum, UUID> stationMap = fetchStationMap();
        stationMap.put(ProducerEnum.valueOf(sensor.getSensorName().toUpperCase()), sensor.getStationId());
        stationDetailsMap.setStationIdMap(stationMap);
    }


    private EnumMap<ProducerEnum, UUID> fetchStationMap() {

        EnumMap<ProducerEnum, UUID> stationMap = stationDetailsMap.getStationIdMap();
        return null == stationMap ? new EnumMap<>(ProducerEnum.class) : stationMap;
    }


    private void saveUserData() throws IOException {
        Resource userResource = null;

        try {
            userResource = resourceLoader.getResource(userFilePath);
            File file = userResource.getFile();
            List<String> userData = Files.readAllLines(file.toPath());

            try (Stream<String> stream = userData.stream()) {

                stream.skip(1).map(line -> line.split(COMMA)).forEach(data -> {
                    UserDTO user = new UserDTO();
                    for (int i = 0; i < data.length; i++) {
                        switch (i) {
                            case 0:
                                user.setLogin(data[i].toLowerCase());
                                break;
                            case 1:
                                user.setEmail(data[i].toLowerCase());
                                break;
                            case 2:
                                user.setPassword(data[i]);
                                break;
                            case 3:
                                user.setAuthorities(new HashSet<>(Arrays.asList(data[i].split(","))));
                                break;
                            case 4:
                                user.setFirstName(data[i].toLowerCase());
                                break;
                            case 5:
                                user.setLastName(data[i].toLowerCase());
                                break;
                            default:
                                user.setLangKey(data[i]);
                                break;
                        }
                    }
                    log.debug("User object to be stored --{}", user);
                    userService.registerUser(user);
                });
            }

        } catch (IOException e) {
            log.error("Error Occurred -> {}", e.getMessage());
        } finally {
            if (userResource != null)
                userResource.readableChannel().close();
        }
    }
}
