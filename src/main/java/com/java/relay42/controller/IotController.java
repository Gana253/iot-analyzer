package com.java.relay42.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relay42/iot")
public class IotController {
    @GetMapping(path = "/collect")
    private String collectData() {
        return "Hello World";
    }
}
