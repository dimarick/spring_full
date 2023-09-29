package ru.students.spring_full.controller;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void home() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpectAll(
                        MockMvcResultMatchers.content().string(StringContains.containsString("List Students")),
                        MockMvcResultMatchers.status().is(200)
                );
    }

    @Test
    void login() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpectAll(
                        MockMvcResultMatchers.content().string(StringContains.containsString("Submit")),
                        MockMvcResultMatchers.status().is(200)
                );
    }

    @Test
    void registerForm() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpectAll(
                        MockMvcResultMatchers.content().string(StringContains.containsString("Registration")),
                        MockMvcResultMatchers.status().is(200)
                );
    }

    @Test
    void register() {
    }

    @Test
    void users() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpectAll(
                        MockMvcResultMatchers.redirectedUrl("http://localhost/login"),
                        MockMvcResultMatchers.status().is(302)
                );
    }
}