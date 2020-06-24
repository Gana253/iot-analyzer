package com.java.relay42;


import com.java.relay42.service.IotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IotAnalyzerApplication implements CommandLineRunner {

    @Autowired
    private IotService iotService;

    public static void main(String[] args) {
        SpringApplication.run(IotAnalyzerApplication.class, args);
    }

    @Override
    public void run(String... params) {
        iotService.loadUsers();
        iotService.consumeIotMessage();
    }

}
