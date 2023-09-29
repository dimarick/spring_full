package ru.students.spring_full.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.students.spring_full.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
