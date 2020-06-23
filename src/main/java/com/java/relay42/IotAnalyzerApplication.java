package com.java.relay42;

import com.java.relay42.jpa.repository.UserRepository;
import com.java.relay42.service.IotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class IotAnalyzerApplication implements CommandLineRunner {
    @Autowired
    UserRepository userRepo;

    @Autowired
    IotService iotService;

    public static void main(String[] args) {
        SpringApplication.run(IotAnalyzerApplication.class, args);
    }

    /*@Bean
    CommandLineRunner init(UserRepository userRepo) {
        return args -> {
            List<String> names = Arrays.asList("Guest", "Guest1");
            names.forEach(name -> userRepo.save(IotUtils.createUser(name)));
        };
    }*/
    @Override
    public void run(String... params) throws Exception {
        List<String> names = Arrays.asList("Guest", "Guest1");
        names.forEach(name -> userRepo.save(iotService.createUser(name)));
    }

}
