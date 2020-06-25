package com.java.relay42.service.impl;

import com.java.relay42.constants.IotConstants;
import com.java.relay42.dto.UserDTO;
import com.java.relay42.entity.User;
import com.java.relay42.exception.InvalidPasswordException;
import com.java.relay42.repository.UserRepository;
import com.java.relay42.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(UserDTO userDTO) {

        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setLangKey(userDTO.getLangKey());
        newUser.setAuthorities(userDTO.getAuthorities());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }


   /* public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        if (userDTO.getLangKey() == null) {
            user.setLangKey(IotConstants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setAuthorities(userDTO.getAuthorities());
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }
*/

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional
                .of(userRepository.findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(
                        user -> {
                            user.setLogin(userDTO.getLogin().toLowerCase());
                            user.setFirstName(userDTO.getFirstName());
                            user.setLastName(userDTO.getLastName());
                            if (userDTO.getEmail() != null) {
                                user.setEmail(userDTO.getEmail().toLowerCase());
                            }
                            user.setLangKey(userDTO.getLangKey());
                            user.setAuthorities(userDTO.getAuthorities());
                            userRepository.save(user);
                            log.debug("Changed Information for User: {}", user);
                            return user;
                        }
                )
                .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
                .findOneByLogin(login)
                .ifPresent(
                        user -> {
                            userRepository.delete(user);
                            log.debug("Deleted User: {}", user);
                        }
                );
    }


    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(
                        user -> {
                            String currentEncryptedPassword = user.getPassword();
                            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                                throw new InvalidPasswordException();
                            }
                            String encryptedPassword = passwordEncoder.encode(newPassword);
                            user.setPassword(encryptedPassword);
                            userRepository.save(user);
                            log.debug("Changed password for User: {}", user);
                        }
                );
    }

    public List<UserDTO> getAllManagedUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> !IotConstants.ANONYMOUS_USER.equals(user.getLogin()))
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }


    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }

    /*public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }*/
}
