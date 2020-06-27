package com.java.relay42.web.rest;


import com.java.relay42.IotAnalyzerApplication;
import com.java.relay42.constants.IotConstants;
import com.java.relay42.dto.PasswordChangeDTO;
import com.java.relay42.dto.UserDTO;
import com.java.relay42.entity.User;
import com.java.relay42.repository.UserRepository;
import com.java.relay42.web.util.TestUtil;
import com.java.relay42.web.util.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = IotAnalyzerApplication.class)
@WithMockUser
public class UserResourceIT {
    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "doejohn";

    private static final String DEFAULT_ID = "id1";

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passdoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    /**
     * Create a User.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(RandomStringUtils.random(60));
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    @BeforeEach
    public void initTest() {
        userRepository.deleteAll();
        user = createEntity();
    }

    @Test
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        UserDTO userDto = new UserDTO();
        userDto.setLogin(DEFAULT_LOGIN);
        userDto.setPassword(DEFAULT_PASSWORD);
        userDto.setFirstName(DEFAULT_FIRSTNAME);
        userDto.setLastName(DEFAULT_LASTNAME);
        userDto.setEmail(DEFAULT_EMAIL);
        userDto.setLangKey(DEFAULT_LANGKEY);
        userDto.setAuthorities(Collections.singleton(IotConstants.ADMIN));

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDto)))
                .andExpect(status().isCreated());

        // Validate the User in the database
        assertPersistedUsers(
                users -> {
                    assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
                    User testUser = users.get(users.size() - 1);
                    assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
                    assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
                    assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
                    assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
                    assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
                    assertThat(testUser.getAuthorities()).containsExactly(IotConstants.ADMIN);
                }
        );
    }

    @Test
    public void createUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        UserDTO userDto = new UserDTO();
        userDto.setId(UUID.randomUUID().toString());
        userDto.setLogin(DEFAULT_LOGIN);
        userDto.setPassword(DEFAULT_PASSWORD);
        userDto.setFirstName(DEFAULT_FIRSTNAME);
        userDto.setLastName(DEFAULT_LASTNAME);
        userDto.setEmail(DEFAULT_EMAIL);
        userDto.setActivated(true);
        userDto.setLangKey(DEFAULT_LANGKEY);
        userDto.setAuthorities(Collections.singleton(IotConstants.ANONYMOUS_USER));

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        userRepository.save(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        UserDTO userDto = new UserDTO();
        userDto.setLogin(DEFAULT_LOGIN); // this login should already be used
        userDto.setPassword(DEFAULT_PASSWORD);
        userDto.setFirstName(DEFAULT_FIRSTNAME);
        userDto.setLastName(DEFAULT_LASTNAME);
        userDto.setEmail("anothermail@localhost");
        userDto.setActivated(true);
        userDto.setLangKey(DEFAULT_LANGKEY);
        userDto.setAuthorities(Collections.singleton(IotConstants.USER));

        // Create the User
        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDto)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        userRepository.save(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        UserDTO userDto = new UserDTO();
        userDto.setLogin("anotherlogin");
        userDto.setPassword(DEFAULT_PASSWORD);
        userDto.setFirstName(DEFAULT_FIRSTNAME);
        userDto.setLastName(DEFAULT_LASTNAME);
        userDto.setEmail(DEFAULT_EMAIL); // this email should already be used
        userDto.setActivated(true);
        userDto.setLangKey(DEFAULT_LANGKEY);
        userDto.setAuthorities(Collections.singleton(IotConstants.USER));

        // Create the User
        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDto)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.save(user);

        // Get all the users
        restUserMockMvc
                .perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    @Test
    public void getUser() throws Exception {
        // Initialize the database
        userRepository.save(user);

        // Get the user
        restUserMockMvc
                .perform(get("/api/users/{login}", user.getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.langKey").value(DEFAULT_LANGKEY));
    }

    @Test
    public void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    public void updateUser() throws Exception {
        // Initialize the database
        userRepository.save(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(updatedUser.getId());
        userDTO.setLogin(updatedUser.getLogin());
        userDTO.setPassword(UPDATED_PASSWORD);
        userDTO.setFirstName(UPDATED_FIRSTNAME);
        userDTO.setLastName(UPDATED_LASTNAME);
        userDTO.setEmail(UPDATED_EMAIL);
        userDTO.setLangKey(UPDATED_LANGKEY);
        userDTO.setAuthorities(Collections.singleton(IotConstants.USER));

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(
                users -> {
                    assertThat(users).hasSize(databaseSizeBeforeUpdate);
                    User testUser = users.get(users.size() - 1);
                    assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
                    assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
                    assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
                    assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
                    assertThat(testUser.getAuthorities()).containsExactly(IotConstants.USER);
                }
        );
    }

    @Test
    public void updateUserLogin() throws Exception {
        // Initialize the database
        userRepository.save(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(updatedUser.getId());
        userDTO.setLogin(UPDATED_LOGIN);
        userDTO.setPassword(UPDATED_PASSWORD);
        userDTO.setFirstName(UPDATED_FIRSTNAME);
        userDTO.setLastName(UPDATED_LASTNAME);
        userDTO.setEmail(UPDATED_EMAIL);
        userDTO.setLangKey(UPDATED_LANGKEY);
        userDTO.setAuthorities(Collections.singleton(IotConstants.ANONYMOUS_USER));

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(
                users -> {
                    assertThat(users).hasSize(databaseSizeBeforeUpdate);
                    User testUser = users.get(users.size() - 1);
                    assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
                    assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
                    assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
                    assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
                    assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
                    assertThat(testUser.getAuthorities()).containsExactly(IotConstants.ANONYMOUS_USER);;
                }
        );
    }

    @Test
    public void updateUserExistingEmail() throws Exception {
        // Initialize the database with 2 users
        userRepository.save(user);

        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID().toString());
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));

        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setLangKey("en");
        userRepository.save(anotherUser);

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(updatedUser.getId());
        userDTO.setLogin(updatedUser.getLogin());
        userDTO.setPassword(updatedUser.getPassword());
        userDTO.setFirstName(updatedUser.getFirstName());
        userDTO.setLastName(updatedUser.getLastName());
        userDTO.setEmail("jhipster@localhost"); // this email should already be used by anotherUser
        userDTO.setLangKey(updatedUser.getLangKey());
        userDTO.setAuthorities(Collections.singleton(IotConstants.USER));

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void deleteUser() throws Exception {
        // Initialize the database
        userRepository.save(user);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc
            .perform(delete("/api/users/{login}", user.getLogin()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeDelete - 1));
    }

    @Test
    public void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setAuthorities(Collections.singleton(IotConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.getAuthorities()).containsExactly(IotConstants.USER);
    }

    @Test
    @WithMockUser("change-password-wrong-existing-password")
    public void testChangePasswordWrongExistingPassword() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-wrong-existing-password");
        user.setEmail("change-password-wrong-existing-password@example.com");
        userRepository.save(user);

        restUserMockMvc
                .perform(
                        post("/api/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO("1" + currentPassword, "new password")))
                )
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-wrong-existing-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(currentPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser("change-password")
    public void testChangePassword() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password");
        user.setEmail("change-password@example.com");
        userRepository.save(user);

        restUserMockMvc
                .perform(
                        post("/api/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, "new password")))
                )
                .andExpect(status().isOk());

        User updatedUser = userRepository.findOneByLogin("change-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser("change-password-too-small")
    public void testChangePasswordTooSmall() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-small");
        user.setEmail("change-password-too-small@example.com");
        userRepository.save(user);

        String newPassword = RandomStringUtils.random(UserDTO.PASSWORD_MIN_LENGTH - 1);

        restUserMockMvc
                .perform(
                        post("/api/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, newPassword)))
                )
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-too-small").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @WithMockUser("change-password-too-long")
    public void testChangePasswordTooLong() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-long");
        user.setEmail("change-password-too-long@example.com");
        userRepository.save(user);

        String newPassword = RandomStringUtils.random(UserDTO.PASSWORD_MAX_LENGTH + 1);

        restUserMockMvc
                .perform(
                        post("/api/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, newPassword)))
                )
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-too-long").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @WithMockUser("change-password-empty")
    public void testChangePasswordEmpty() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-empty");
        user.setEmail("change-password-empty@example.com");
        userRepository.save(user);

        restUserMockMvc
                .perform(
                        post("/api/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, "")))
                )
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-empty").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }
    private void assertPersistedUsers(Consumer<List<User>> userAssertion) {
        userAssertion.accept(userRepository.findAll());
    }
}
