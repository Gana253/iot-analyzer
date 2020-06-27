package com.java.relay42.web.rest;

import com.java.relay42.dto.JwtRequest;
import com.java.relay42.entity.User;
import com.java.relay42.exception.BadRequestAlertException;
import com.java.relay42.repository.UserRepository;
import com.java.relay42.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing authenticating the User
 */
@RestController
@CrossOrigin
public class JwtAuthenticationResource {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    /**
     * {@code POST  /JwtRequest} : Authorize the user.
     * @param authenticationRequest  UserName and Password of the user
     * @return JwtToken if authenticated
     * @throws Exception Throws UnAuthorized exception
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername().toLowerCase(), authenticationRequest.getPassword()));
            return ResponseEntity.ok(jwtTokenUtil.createToken(authenticationRequest.getUsername().toLowerCase(), getAuthorities(authenticationRequest)));
        } catch (AuthenticationException e) {
            throw new BadRequestAlertException("Authentication Denied", "Iot Anlayzer", "Invalid Username/Password");
        }
    }

    private Set<String> getAuthorities(@RequestBody JwtRequest authenticationRequest) {
        Optional<User> user = userRepository.findOneByLogin(authenticationRequest.getUsername().toLowerCase());

        return user.orElse(new User()).getAuthorities();
    }
}
