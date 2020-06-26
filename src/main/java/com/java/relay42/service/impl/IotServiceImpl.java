package com.java.relay42.service.impl;

import com.datastax.driver.core.utils.UUIDs;
import com.java.relay42.config.ProducerEnum;
import com.java.relay42.config.StationDetailsMap;
import com.java.relay42.dto.UserDTO;
import com.java.relay42.entity.Device;
import com.java.relay42.entity.Readings;
import com.java.relay42.entity.ReadingsPrimaryKey;
import com.java.relay42.repository.DeviceRepository;
import com.java.relay42.repository.ReadingsRepository;
import com.java.relay42.service.IotService;
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
 *
 */
@Service
public class IotServiceImpl implements IotService {

    public static final String COMMA = ";";

    private static final Logger log = LoggerFactory.getLogger(IotServiceImpl.class);

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

    @Value("${inputfile.name}")
    private String filePath; //Csv file input path

    public void loadUsersAndAuthorities() throws IOException {
        saveUserData();
        loadDeviceData();
    }

    public void buildReadingObjForPersist(Integer reading, ProducerEnum stationType) {
        Readings readingObj = new Readings();
        readingObj.setId(UUIDs.timeBased());
        readingObj.setReading(Double.valueOf(reading));

        readingObj.setKey(new ReadingsPrimaryKey(stationDetailsMap.getStationIdMap().get(stationType)));
        readingsRepository.save(readingObj);

    }


    private void loadDeviceData() throws IOException {
        Resource deviceResource = null;

        try {
            deviceResource = resourceLoader.getResource("classpath:dataset/devicelist.csv");
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
            e.printStackTrace();
        } finally {
            if (null != deviceResource) deviceResource.readableChannel().close();
        }
    }


    private void setStationDetails(Device device) {
        log.info("updating the tracker object with {}", device.getStationName());
        Map<ProducerEnum, UUID> stationMap = fetchStationMap();
        stationMap.put(ProducerEnum.valueOf(device.getDeviceName().toUpperCase()), device.getStationId());
        stationDetailsMap.setStationIdMap(stationMap);
    }


    private Map<ProducerEnum, UUID> fetchStationMap() {

        Map<ProducerEnum, UUID> stationMap = stationDetailsMap.getStationIdMap();
        return null == stationMap ? new HashMap<>() : stationMap;
    }/*

    public void setReading() {
        Device device = new Device();
        UUID deviceId = UUIDs.timeBased();
        device.setId(deviceId);
        device.setClientName("Test");
        device.setDeviceName("Device1");
        device.setLocation("UK");
        device.setUnit("Celsius");
        UUID station = UUIDs.timeBased();
        device.setStationId(station);
        device.setStationName("Belfast");
        Readings reading = setReadings(station, "90");
        Readings reading1 = setReadings(station, "91");
        readingsRepository.save(reading);
        readingsRepository.save(reading1);
        deviceRepository.save(device);

    }*/

    /*private Readings setReadings(UUID stationId, String value) {
        Readings reading = new Readings();
        ReadingsPrimaryKey rPK = new ReadingsPrimaryKey();
        rPK.setStationId(stationId);
        reading.setId(UUIDs.timeBased());
        reading.setKey(rPK);
        reading.setReading(new BigInteger(value));
        return reading;
    }
*/


    /**
     * Method to read the CSV file and create user list
     */
    private void saveUserData() throws IOException {
        Resource userResource = null;

        try {
            userResource = resourceLoader.getResource(filePath);
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
