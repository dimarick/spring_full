package ru.students.spring_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.students.spring_security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
