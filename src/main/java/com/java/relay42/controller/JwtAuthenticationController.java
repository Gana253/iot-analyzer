package com.java.relay42.controller;

import com.java.relay42.dto.JwtRequest;
import com.java.relay42.entity.User;
import com.java.relay42.exception.CustomException;
import com.java.relay42.repository.UserRepository;
import com.java.relay42.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername().toLowerCase(), authenticationRequest.getPassword()));
            return ResponseEntity.ok(jwtTokenUtil.createToken(authenticationRequest.getUsername().toLowerCase(), getAuthorities(authenticationRequest)));
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private Set<String> getAuthorities(@RequestBody JwtRequest authenticationRequest) {
        Optional<User> user = userRepository.findOneByLogin(authenticationRequest.getUsername().toLowerCase());

        return user.orElse(new User()).getAuthorities();
    }
}
