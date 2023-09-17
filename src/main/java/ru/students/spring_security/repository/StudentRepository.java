package ru.students.spring_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.students.spring_security.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
