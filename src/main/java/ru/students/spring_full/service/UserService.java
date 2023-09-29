package ru.students.spring_full.service;

import org.springframework.stereotype.Service;
import ru.students.spring_full.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    void saveUser(UserDto userDto);
    UserDto findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
