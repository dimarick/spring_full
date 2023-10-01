package ru.students.spring_full.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.students.spring_full.entity.Role;
import ru.students.spring_full.entity.User;
import ru.students.spring_full.repository.RoleRepository;
import ru.students.spring_full.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomUserDetailsServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static User user;

    @BeforeAll
    public static void setUpClass() {
        user = null;
    }

    @BeforeEach
    public void setUp() {
        if (user == null) {
            var roles = List.of(
                    roleRepository.save(new Role(null, "ROLE_USER", new ArrayList<>())),
                    roleRepository.save(new Role(null, "ROLE_ADMIN", new ArrayList<>()))
            );

            user = new User(null, "user", "user@example.com", "1", roles);
            userRepository.save(user);
        }
    }

    @Test
    public void loadUserByUsername() {
        var user = customUserDetailsService.loadUserByUsername("user@example.com");

        assertEquals("user@example.com", user.getUsername());
        assertEquals("1", user.getPassword());
        assertArrayEquals(new String[]{"ROLE_ADMIN", "ROLE_USER"}, user.getAuthorities().stream().map(a -> a.getAuthority()).sorted().toArray());
    }
}