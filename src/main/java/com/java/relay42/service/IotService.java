package com.java.relay42.service;


import java.io.IOException;

public interface IotService {
    void loadUsersAndAuthorities() throws IOException;

    void consumeIotMessage();
}
