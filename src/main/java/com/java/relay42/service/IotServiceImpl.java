package com.java.relay42.service;

import com.java.relay42.model.Role;
import com.java.relay42.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class IotServiceImpl implements IotService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String userName) {
        User usr = new User();
        usr.setUsername(userName);
        usr.setPassword(passwordEncoder.encode(userName));
        usr.setEmail("default@abc.com");
        usr.setRoles(new ArrayList<>(Arrays.asList(Role.ROLE_ADMIN)));
        return usr;
    }
}
