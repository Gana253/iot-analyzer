package com.java.relay42.service;


import com.java.relay42.config.ProducerEnum;

import java.io.IOException;

public interface IotService {
    void loadUsersAndAuthorities() throws IOException;

    void buildReadingObjForPersist(Integer reading, ProducerEnum stationType);
}
