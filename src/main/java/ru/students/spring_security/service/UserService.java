package ru.students.spring_security.service;

import org.springframework.stereotype.Service;
import ru.students.spring_security.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    void saveUser(UserDto userDto);
    UserDto findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
