package com.java.relay42.service.impl;

import com.datastax.driver.core.utils.UUIDs;
import com.java.relay42.dto.UserDTO;
import com.java.relay42.entity.Authority;
import com.java.relay42.entity.Device;
import com.java.relay42.entity.Readings;
import com.java.relay42.entity.ReadingsPrimaryKey;
import com.java.relay42.model.Person;
import com.java.relay42.repository.AuthorityRepository;
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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IotServiceImpl implements IotService {

    public static final String COMMA = ";";

    private static final Logger log = LoggerFactory.getLogger(IotServiceImpl.class);

    private String baseUrl = "http://localhost:8085";

    private WebClient client = WebClient.create(baseUrl);


    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ReadingsRepository readingsRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    @Value("${inputfile.name}")
    private String filePath; //Csv file input path


    private static void logTime(Instant start) {
        log.debug("Elapsed time: " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }

    public void loadUsersAndAuthorities() throws IOException {
        saveUserData();
        saveAuthority();
        loadDeviceData();
        setReading();
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
                    deviceRepository.save(device);
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (deviceResource.isOpen()) deviceResource.readableChannel().close();
        }
    }

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

    }

    private Readings setReadings(UUID stationId, String value) {
        Readings reading = new Readings();
        ReadingsPrimaryKey rPK = new ReadingsPrimaryKey();
        rPK.setStationId(stationId);
        rPK.setId(UUIDs.timeBased());
        reading.setKey(rPK);
        reading.setReading(value);
        return reading;
    }

    public void consumeIotMessage() {
        Instant start = Instant.now();

        List<Mono<Person>> list = Stream.of(1, 2, 3, 4, 5)
                .map(i -> client.get().uri("/person/{id}", i).retrieve().bodyToMono(Person.class))
                .collect(Collectors.toList());

        Mono.when(list).block();
    }

    private void saveAuthority() {

        Resource authorityResource = resourceLoader.getResource("classpath:dataset/authority.csv");

        try {
            File file = authorityResource.getFile();
            List<String> authorityData = Files.readAllLines(file.toPath());

            try (Stream<String> stream = authorityData.stream()) {
                stream.skip(1).forEach(data -> authorityRepository.save(new Authority(data)));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Method to read the CSV file and create user list
     *
     * @return
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
            if (userResource.isOpen())
                userResource.readableChannel().close();
        }
    }
}
