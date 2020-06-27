package com.java.relay42.service;

import com.datastax.driver.core.utils.UUIDs;
import com.java.relay42.config.ProducerEnum;
import com.java.relay42.config.StationDetailsMap;
import com.java.relay42.dto.UserDTO;
import com.java.relay42.entity.Device;
import com.java.relay42.entity.Readings;
import com.java.relay42.entity.ReadingsPrimaryKey;
import com.java.relay42.repository.DeviceRepository;
import com.java.relay42.repository.ReadingsRepository;
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
    private DeviceRepository deviceRepository;

    @Autowired
    private ReadingsRepository readingsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StationDetailsMap stationDetailsMap;

    @Value("${inputfile.user-name}")
    private String userFilePath; //Csv file input path

    @Value("${inputfile.device-name}")
    private String deviceFilePath; //Csv file input path

    /**
     * Load User data and Device data on startup
     *
     * @throws IOException when CSV file is not found
     */
    public void loadUsersAndDeviceData() throws IOException {
        saveUserData();
        loadDeviceData();
    }

    /**
     * Assists consumer to save the reading value received.
     *
     * @param reading     Reading value and Station Type
     * @param stationType
     */
    public void buildReadingObjForPersist(Integer reading, ProducerEnum stationType) {
        Readings readingObj = new Readings();
        readingObj.setId(UUIDs.timeBased());
        readingObj.setReading(Double.valueOf(reading));

        readingObj.setKey(new ReadingsPrimaryKey(stationDetailsMap.getStationIdMap().get(stationType)));
        readingsRepository.save(readingObj);

    }

    /**
     * Load device from the device.csv file and persist in table
     *
     * @throws IOException when file is not found
     */
    private void loadDeviceData() throws IOException {
        Resource deviceResource = null;

        try {
            deviceResource = resourceLoader.getResource(deviceFilePath);
            File file = deviceResource.getFile();
            List<String> userData = Files.readAllLines(file.toPath());

            try (Stream<String> stream = userData.stream()) {
                stream.skip(1).map(line -> line.split(COMMA)).forEach(data -> {
                    Device device = new Device();
                    device.setId(UUIDs.timeBased());
                    device.setStationId(UUIDs.timeBased());
                    device.setCreatedBy("SYSTEM");
                    for (int i = 0; i < data.length; i++) {
                        switch (i) {
                            case 0:
                                device.setDeviceName(data[i].toLowerCase());
                                break;
                            case 1:
                                device.setClientName(data[i].toLowerCase());
                                break;
                            case 2:
                                device.setStationName(data[i].toLowerCase());
                                break;
                            case 3:
                                device.setLocation(data[i].toLowerCase());
                                break;
                            default:
                                device.setUnit(data[i].toLowerCase());
                                break;
                        }
                    }
                    //
                    setStationDetails(device);
                    deviceRepository.save(device);
                });
            }

        } catch (IOException e) {
            log.error("Exception while parsing device data--", e.getMessage());
        } finally {
            if (null != deviceResource) deviceResource.readableChannel().close();
        }
    }

    /**
     * Track the station details data with station id
     *
     * @param device Device with DeviceName(Thermostat,FuelReading,HearRateReader) and Station ID
     */
    private void setStationDetails(Device device) {
        log.info("updating the tracker object with {}", device.getStationName());
        Map<ProducerEnum, UUID> stationMap = fetchStationMap();
        stationMap.put(ProducerEnum.valueOf(device.getDeviceName().toUpperCase()), device.getStationId());
        stationDetailsMap.setStationIdMap(stationMap);
    }


    private Map<ProducerEnum, UUID> fetchStationMap() {

        Map<ProducerEnum, UUID> stationMap = stationDetailsMap.getStationIdMap();
        return null == stationMap ? new HashMap<>() : stationMap;
    }


    /**
     * Load User from the user.csv file and persist in table
     *
     * @throws IOException when file is not found
     */
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
