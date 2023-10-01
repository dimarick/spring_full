package ru.students.spring_full.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.students.spring_full.dto.UserDto;
import ru.students.spring_full.entity.Role;
import ru.students.spring_full.entity.User;
import ru.students.spring_full.repository.RoleRepository;
import ru.students.spring_full.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        var user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = createAdminRole();
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        var user = userRepository.findByEmail(email);
        return mapToDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(UserServiceImpl::mapToDto).toList();
    }

    private static UserDto mapToDto(User user) {
        if (user == null) {
            return null;
        }

        var nameParts = user.getName().split(" ");
        return new UserDto(user.getId(), nameParts[0], nameParts.length > 1 ? nameParts[1] : "", user.getEmail(), user.getPassword());
    }

    private Role createAdminRole() {
        Role role;
        role = new Role();
        role.setName("ROLE_ADMIN");
        role = roleRepository.save(role);
        return role;
    }
}
