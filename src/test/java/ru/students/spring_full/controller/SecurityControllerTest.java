package ru.students.spring_full.controller;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.students.spring_full.entity.Role;
import ru.students.spring_full.entity.User;
import ru.students.spring_full.repository.RoleRepository;
import ru.students.spring_full.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static User user;

    @BeforeAll
    public static void setUpClass() {
        user = null;
    }

    @BeforeEach
    public void setUp() {
        if (user == null) {
            var roles = List.of(
                    roleRepository.save(new Role(null, "ROLE_USER", new ArrayList<>()))
            );

            user = new User(null, "user", "user@example.com", "$2a$10$J3sKcZd6fjEeQaxo64bqjOzsySnSY9ZzDkyNeZM5c1z9czvtkFBXK", roles);
            userRepository.save(user);
        }
    }

    @Test
    void home() throws Exception {
        mvc.perform(get("/"))
            .andExpectAll(
                redirectedUrl("http://localhost/login"),
                status().is(302)
            );
        mvc.perform(get("/").with(user("user@example.com").roles("USER")))
            .andExpectAll(
                content().string(StringContains.containsString("Не забыть купить")),
                status().is(200)
            );
    }

    @Test
    void login() throws Exception {
        mvc.perform(get("/login"))
                .andExpectAll(
                        content().string(StringContains.containsString("Вход")),
                        status().is(200)
                );
        mvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("username=user%40example.com&password=123")
            )
            .andExpectAll(
                redirectedUrl("/users"),
                status().is(302)
            );

        mvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("username=user%40example.com&password=1234")
            )
            .andExpectAll(
                redirectedUrl("/login?error"),
                status().is(302)
            );
    }

    @Test
    void registerForm() throws Exception {
        mvc.perform(get("/register"))
            .andExpectAll(
                content().string(StringContains.containsString("Registration")),
                status().is(200)
            );
    }

    @Test
    void register() throws Exception {
        mvc.perform(post("/register/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("firstName=Dmitrii&lastName=Ivanov&email=user2%40example.com&password=123"))
            .andExpectAll(
                redirectedUrl("/register?success"),
                status().is(302)
            );

        mvc.perform(post("/register/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("firstName=Dmitrii&lastName=Petrov&email=user3%40example.com&password=1234"))
            .andExpectAll(
                redirectedUrl("/register?success"),
                status().is(302)
            );

        mvc.perform(post("/register/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("firstName=Dmitrii&lastName=Sidorov&email=user3%40example.com&password=12345"))
            .andExpectAll(
                    content().string(StringContains.containsString("Registration")),
                status().is(200)
            );

        var user1 = userRepository.findByEmail("user@example.com");
        var user2 = userRepository.findByEmail("user2@example.com");
        var user3 = userRepository.findByEmail("user3@example.com");

        assertNotEquals(user1.getPassword(), user2.getPassword());

        var passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches("123", user1.getPassword()));
        assertTrue(passwordEncoder.matches("123", user2.getPassword()));
        assertTrue(passwordEncoder.matches("1234", user3.getPassword()));

        assertEquals("Dmitrii Ivanov", user2.getName());
        assertEquals("Dmitrii Petrov", user3.getName());
    }

    @Test
    void users() throws Exception {
        mvc.perform(get("/users"))
            .andExpectAll(
                redirectedUrl("http://localhost/login"),
                status().is(302)
            );

        mvc.perform(get("/users").with(user("user@example.com").roles("USER")))
            .andExpectAll(
                content().string(StringContains.containsString("List of Registered Users")),
                content().string(StringContains.containsString("user@example.com")),
                status().is(200)
            );
    }
}