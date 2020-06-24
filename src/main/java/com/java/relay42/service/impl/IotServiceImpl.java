package com.java.relay42.service.impl;

import com.datastax.driver.core.utils.UUIDs;
import com.java.relay42.model.Person;
import com.java.relay42.model.Role;
import com.java.relay42.entity.User;
import com.java.relay42.repository.UserRepository;
import com.java.relay42.service.IotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IotServiceImpl implements IotService {

    public static final String TAB = ";";

    private static final Logger log = LoggerFactory.getLogger(IotServiceImpl.class);

    private  String baseUrl = "http://localhost:8085";

    private  WebClient client = WebClient.create(baseUrl);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UserRepository userRepo;

    @Value("${inputfile.name}")
    private String filePath; //Tsv file input path

    private List<User> userList;

    public void loadUsers() {
        userList = retrieveUserDtls();
        userList.forEach(user -> userRepo.save(user));
    }

    public void consumeIotMessage(){
        Instant start = Instant.now();

        List<Mono<Person>> list = Stream.of(1, 2, 3, 4, 5)
                .map(i -> client.get().uri("/person/{id}", i).retrieve().bodyToMono(Person.class))
                .collect(Collectors.toList());

        Mono.when(list).block();
    }
    private static void logTime(Instant start) {
        log.debug("Elapsed time: " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }


    /**
     * Method to read the TSV file and create user list
     *
     * @return
     */
    private List<User> retrieveUserDtls() {

        List<User> userLst = new ArrayList<>();

        Resource userResource = resourceLoader.getResource(filePath);

        try {
            File file = userResource.getFile();
            List<String> ratingsData = Files.readAllLines(file.toPath());

            try (Stream<String> stream = ratingsData.stream()) {

                userLst = stream.skip(1).map(line -> line.split(TAB)).map(data -> {
                    User user = new User();
                    user.setId(UUIDs.timeBased());
                    for (int i = 0; i < data.length; i++) {
                        switch (i) {
                            case 0:
                                user.setUsername(data[i].toUpperCase());
                                break;
                            case 1:
                                user.setEmail(data[i].toUpperCase());
                                break;
                            case 2:
                                user.setPassword(passwordEncoder.encode(data[i]));
                                break;
                            default:
                                user.setRoles(fetchRoles(Arrays.asList(data[i].split(","))));
                                break;
                        }
                    }
                    return user;
                }).collect(Collectors.toList());
            }

        } catch (IOException e) {
            log.error("Error Occurred -> {}", e.getMessage());
        }

        return userLst;

    }

    private List<Role> fetchRoles(List<String> roles) {
        return roles.stream().map(r -> Role.valueOf(r.toUpperCase())).collect(Collectors.toList());
    }
}
