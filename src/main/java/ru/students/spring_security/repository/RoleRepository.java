package ru.students.spring_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.students.spring_security.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
